package net.codelet.cloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.annotation.InternalAccessOnly;
import net.codelet.cloud.annotation.ValidateCaptcha;
import net.codelet.cloud.annotation.ValidateVerificationCode;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.dto.response.JsonApiDTO;
import net.codelet.cloud.dto.response.JsonApiErrorDTO;
import net.codelet.cloud.dto.response.JsonApiObjectDTO;
import net.codelet.cloud.dto.response.JsonApiPrimitiveDTO;
import net.codelet.cloud.error.*;
import net.codelet.cloud.util.ReflectionUtils;
import net.codelet.cloud.util.StringUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * 控制器基类。
 */
@RestControllerAdvice
public abstract class BaseController extends HttpContext {

    // 控制器标签与控制器类型名称的映射表
    private static final Map<String, String> controllerTags = new HashMap<>();

    private Map<String, String> apiTagMap = new HashMap<>();

    /**
     * 构造方法。
     */
    public BaseController() {
        // 取得控制器的类型
        final Class<?> controllerType = this.getClass();

        // 将控制器方法所实现的 API 中定义的方法的 InternalAccessOnly 注解添加到控制器方法的注解列表中
        ReflectionUtils.inheritMethodAnnotationsFromInterface(
            controllerType,
            interfaceType -> interfaceType.getDeclaredAnnotation(FeignClient.class) != null,
            method -> method.getDeclaredAnnotation(ExceptionHandler.class) == null,
            InternalAccessOnly.class
        );

        // 当为控制器设置了多个 API 标签时，将这些标签合并成一条
        Annotation apiAnnotation = controllerType.getDeclaredAnnotation(Api.class);
        if (apiAnnotation != null) {
            Map<String, Object> properties = ReflectionUtils.getAnnotationMemberValues(apiAnnotation);
            List<String> tags = new ArrayList<>(Arrays.asList((String[]) properties.get("tags")));
            Collections.sort(tags);

            tags.forEach(tag -> {
                String[] kvp = tag.split("=");
                if (kvp.length == 2) {
                    apiTagMap.put(kvp[0], kvp[1]);
                } else {
                    apiTagMap.put(tag, "");
                }
            });
            apiTagMap.put("controller", controllerType.getName());

            String tag = StringUtils.toJSON(apiTagMap);

            // 若控制器无 API 标签则抛出异常
            if (StringUtils.isEmpty(tag)) {
                String errorMessage = String.format("REST controller %s has no API tag.", controllerType.getTypeName());
                System.err.println(errorMessage);
                throw new RuntimeException(errorMessage);
            }

            // 若相同的 API 标签已为其他控制器设置则抛出异常
            if (controllerTags.containsKey(tag)) {
                String errorMessage = String.format(
                    "REST controller %s has the same API tags with REST controller %s. Tags: %s.",
                    controllerTags.get(tag),
                    controllerType.getTypeName(),
                    String.join(", ", tags)
                );
                System.err.println(errorMessage);
                throw new RuntimeException(errorMessage);
            }

            // 否则更新控制器的 API 标签
            properties.put("tags", new String[]{tag});
            controllerTags.put(tag, controllerType.getTypeName());
        }

        for (Method handler : controllerType.getDeclaredMethods()) {
            if (!Modifier.isPublic(handler.getModifiers())) {
                continue;
            }

            ApiOperation apiOperationAnnotation = handler.getDeclaredAnnotation(ApiOperation.class);
            Map<String, Object> apiOperationMemberValues = ReflectionUtils.getAnnotationMemberValues(apiOperationAnnotation);
            if (apiOperationAnnotation == null || apiOperationMemberValues.get("$modified") != null) {
                continue;
            }

            // 设置请求参数的必要性注解
            for (Parameter parameter : handler.getParameters()) {
                setApiModelPropertyConstraints(parameter, parameter.getType(), ApiParam.class);
                if (parameter.getAnnotation(Valid.class) != null) {
                    setApiModelPropertyConstraints(parameter.getType());
                }
            }

            // 更新方法的 API 注解的标签，添加控制器方法信息
            List<String> parameterTypeNames = new ArrayList<>();
            for (Class parameterType : handler.getParameterTypes()) {
                parameterTypeNames.add(parameterType.getName());
            }
            apiOperationMemberValues.put("nickname", String.format(
                "%s %s(%s)",
                handler.getGenericReturnType(),
                handler.getName(),
                String.join(", ", parameterTypeNames)
            ));

            Map<String, Object> handlerInfo = new HashMap<>();
            handlerInfo.put("description", apiOperationMemberValues.get("notes"));
            Annotation handlerAnnotation;

            handlerAnnotation = handler.getDeclaredAnnotation(InternalAccessOnly.class);
            if (handlerAnnotation != null) {
                handlerInfo.put("internalAccessOnly", true);
            }

            handlerAnnotation = handler.getDeclaredAnnotation(CheckUserPrivilege.class);
            if (handlerAnnotation != null) {
                Map<String, Object> checkUserPrivilegeMemberValues = ReflectionUtils.getAnnotationMemberValues(handlerAnnotation);
                handlerInfo.put("accessTokenRequired", checkUserPrivilegeMemberValues.get("required"));
                handlerInfo.put("administrative", checkUserPrivilegeMemberValues.get("administrator"));
                handlerInfo.put("ownership", checkUserPrivilegeMemberValues.get("owner"));
                handlerInfo.put("membership", checkUserPrivilegeMemberValues.get("member"));
            }

            handlerAnnotation = handler.getDeclaredAnnotation(ValidateCaptcha.class);
            if (handlerAnnotation != null) {
                handlerInfo.put("captchaRequired", ReflectionUtils.getAnnotationMemberValues(handlerAnnotation).get("value"));
            }

            handlerAnnotation = handler.getDeclaredAnnotation(ValidateVerificationCode.class);
            if (handlerAnnotation != null) {
                handlerInfo.put("verificationCode", ReflectionUtils.getAnnotationMemberValues(handlerAnnotation).get("value"));
            }

            apiOperationMemberValues.put("notes", StringUtils.toJSON(handlerInfo));
            apiOperationMemberValues.put("$modified", true);
        }
    }

    /**
     * 设置请求参数的约束信息。
     * @param type 请求参数的类型
     */
    private void setApiModelPropertyConstraints(Class type) {
        if (type.isPrimitive()) {
            return;
        }
        for (Field field: ReflectionUtils.getFields(type)) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            setApiModelPropertyConstraints(field, field.getType(), ApiModelProperty.class);
        }
    }

    /**
     * 设置请求参数的约束信息。
     * @param parameter      请求参数
     * @param type           请求参数的类型
     * @param annotationType 文档注解的类型
     */
    private void setApiModelPropertyConstraints(
        AnnotatedElement parameter,
        Class type,
        Class<? extends Annotation> annotationType
    ) {
        Annotation annotation = parameter.getAnnotation(annotationType);
        if (annotation == null) {
            return;
        }

        Map<String, Object> annotationMemberValues = ReflectionUtils.getAnnotationMemberValues(annotation);

        if (annotationMemberValues.get("$modified") != null) {
            return;
        }

        if (!((boolean) annotationMemberValues.get("required"))
            && (type.isPrimitive()
                || parameter.getAnnotation(NotNull.class) != null
                || parameter.getAnnotation(NotEmpty.class) != null
                || parameter.getAnnotation(NotBlank.class) != null)) {
            annotationMemberValues.put("required", true);
        }

        Map<String, Object> annotationValue = new HashMap<>();
        annotationValue.put("typeName", type.getTypeName());
        if (StringUtils.isEmpty((String) annotationMemberValues.get("value"))) {
            annotationValue.put("description", annotationMemberValues.get("notes"));
        } else {
            annotationValue.put("description", annotationMemberValues.get("value"));
        }

        Digits digitsAnnotation = parameter.getAnnotation(Digits.class);
        if (digitsAnnotation != null) {
            annotationValue.put("digits", new int[] {digitsAnnotation.integer(), digitsAnnotation.fraction()});
        }

        DecimalMin decMinAnnotation = parameter.getAnnotation(DecimalMin.class);
        if (decMinAnnotation != null) {
            annotationValue.put("decimalMin", decMinAnnotation.value());
        }

        DecimalMax decMaxAnnotation = parameter.getAnnotation(DecimalMax.class);
        if (decMaxAnnotation != null) {
            annotationValue.put("decimalMax", decMaxAnnotation.value());
        }

        Min minAnnotation = parameter.getAnnotation(Min.class);
        if (minAnnotation != null) {
            annotationValue.put("min", minAnnotation.value());
        }

        Max maxAnnotation = parameter.getAnnotation(Max.class);
        if (maxAnnotation != null) {
            annotationValue.put("max", maxAnnotation.value());
        }

        if (parameter.getAnnotation(Negative.class) != null) {
            annotationValue.put("negative", true);
        }

        if (parameter.getAnnotation(NegativeOrZero.class) != null) {
            annotationValue.put("negative", true);
            annotationValue.put("zero", true);
        }

        if (parameter.getAnnotation(Positive.class) != null) {
            annotationValue.put("positive", true);
        }

        if (parameter.getAnnotation(PositiveOrZero.class) != null) {
            annotationValue.put("positive", true);
            annotationValue.put("zero", true);
        }

        Size sizeAnnotation = parameter.getAnnotation(Size.class);
        if (sizeAnnotation != null) {
            annotationValue.put("size", new int[] {sizeAnnotation.min(), sizeAnnotation.max()});
        }

        if (parameter.getAnnotation(Email.class) != null) {
            annotationValue.put("email", true);
        }

        Pattern patternAnnotation = parameter.getAnnotation(Pattern.class);
        if (patternAnnotation != null) {
            Map<String, Object> pattern = new HashMap<>();
            pattern.put("regexp", patternAnnotation.regexp());
            pattern.put("flags", patternAnnotation.flags());
            annotationValue.put("pattern", pattern);
        }

        if (parameter.getAnnotation(Past.class) != null) {
            annotationValue.put("past", true);
        }

        if (parameter.getAnnotation(PastOrPresent.class) != null) {
            annotationValue.put("past", true);
            annotationValue.put("present", true);
        }

        if (parameter.getAnnotation(Future.class) != null) {
            annotationValue.put("future", true);
        }

        if (parameter.getAnnotation(FutureOrPresent.class) != null) {
            annotationValue.put("future", true);
            annotationValue.put("present", true);
        }

        annotationMemberValues.put("value", StringUtils.toJSON(annotationValue));
        annotationMemberValues.put("$modified", true);

        setApiModelPropertyConstraints(type);
    }

    /**
     * 用户授权错误处理。
     * 用户授权错误包括：未登录、登录凭证不正确、访问令牌过期、访问令牌无效等。
     * @param error 错误信息
     * @return 返回结果
     */
    @ExceptionHandler(value = UnauthorizedError.class)
    public JsonApiDTO unauthorizedErrorHandler(UnauthorizedError error) {
        return baseErrorHandler(error);
    }

    /**
     * 访问拒绝错误处理。
     * 访问拒绝错误包括：请求头无效、权限不足等。
     * @param error 错误信息
     * @return 返回结果
     */
    @ExceptionHandler(value = AccessDeniedError.class)
    public JsonApiDTO accessDeniedErrorHandler(AccessDeniedError error) {
        return baseErrorHandler(error);
    }

    /**
     * 资源不存在错误处理。
     * @param error 错误信息
     * @return 返回结果
     */
    @ExceptionHandler(value = NotFoundError.class)
    public JsonApiDTO notFoundErrorHandler(NotFoundError error) {
        return baseErrorHandler(error);
    }

    /**
     * 表单验证错误处理。
     * @param error 错误信息
     * @return 返回结果
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    private JsonApiDTO methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException error) {
        return baseErrorHandler(new ValidationError(error));
    }

    /**
     * 错误处理。
     * @param error 错误信息
     * @return 返回结果
     */
    @ExceptionHandler(value = BaseError.class)
    private JsonApiDTO baseErrorHandler(BaseError error) {
        Object data = error.getData();
        if (data != null) {
            if (data instanceof BaseDTO) {
                JsonApiObjectDTO result = new JsonApiObjectDTO<>(getContext(), error);
                // noinspection unchecked
                result.setData((BaseDTO) data);
                return result;
            } else if (data.getClass().isPrimitive()
                || data instanceof Number
                || data instanceof Boolean
            ) {
                JsonApiPrimitiveDTO result = new JsonApiPrimitiveDTO<>(getContext(), error);
                // noinspection unchecked
                result.setData(data);
                return result;
            }
        }
        return new JsonApiDTO(getContext(), error);
    }

    /**
     * HTTP 响应错误。
     * @param error 错误信息
     * @return 返回结果
     */
    @ExceptionHandler(value = JsonApiErrorDTO.class)
    public JsonApiDTO jsonApiErrorHandler(JsonApiErrorDTO error) {
        return new JsonApiDTO(getContext(), error);
    }

    /**
     * FeignClient 响应错误。
     * @param error 错误信息
     * @return 返回结果
     */
    @ExceptionHandler(value = FeignBadRequestError.class)
    public JsonApiDTO feignBadRequestErrorHandler(FeignBadRequestError error) {
        return new JsonApiDTO(getContext(), error.getError());
    }

    /**
     * 异常处理。
     * @param error 错误信息
     * @return 返回结果
     */
    @ExceptionHandler(value = Exception.class)
    private JsonApiDTO exceptionHandler(Exception error) {
        error.printStackTrace(System.err);
        return new JsonApiDTO(getContext(), error);
    }

}
