package net.codelet.cloud.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.dto.response.*;
import net.codelet.cloud.util.RequestContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static net.codelet.cloud.constant.HttpRequestAttributes.REFERENCED_ENTITIES;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * HTTP 消息转换器配置。
 * 将控制器方法的返回结果包装为 JSON API 的形式。
 */
@Configuration
@ControllerAdvice
public class HttpMessageConverterConfiguration implements WebMvcConfigurer, ResponseBodyAdvice<Object> {

    private final byte[] successResponseBody;
    private final ObjectMapper objectMapper;

    @Autowired
    public HttpMessageConverterConfiguration(ObjectMapper objectMapper) throws JsonProcessingException {
        this.objectMapper = objectMapper;
        successResponseBody = objectMapper.writeValueAsBytes(new JsonApiDTO());
    }

    /**
     * 添加用于在控制器无返回值时生成 JSON API 的拦截器。
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public void postHandle(
                HttpServletRequest request,
                HttpServletResponse response,
                Object handler,
                @Nullable ModelAndView modelAndView
            ) {
                if (handler instanceof HandlerMethod
                    && ((HandlerMethod) handler).getReturnType().getParameterType() == void.class) {
                    writeJsonApiWithNoContent(response);
                }
            }
        });
    }

    /**
     * 是否处理当前类型：处理除 void 以外的所有返回类型。
     * @param returnType    返回结果类型
     * @param converterType 转换器类型
     * @return 是否处理当前类型
     */
    @Override
    public boolean supports(
        @NonNull MethodParameter returnType,
        @NonNull Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return !returnType.getParameterType().isAssignableFrom(void.class);
    }

    /**
     * 当控制器方法的返回结果为 null 时返回 JSON API 形式的数据。
     * 默认情况下若控制器方法返回的结果为 null 那么消息转换器将不会被执行。
     * @param body                  返回结果
     * @param returnType            返回结果类型
     * @param selectedContentType   内容类型
     * @param selectedConverterType 转换器类型
     * @param request               HTTP 请求
     * @param response              HTTP 响应
     * @return 转换后的数据
     */
    @Override
    public Object beforeBodyWrite(
        Object body,
        @NonNull MethodParameter returnType,
        @NonNull MediaType selectedContentType,
        @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
        @NonNull ServerHttpRequest request,
        @NonNull ServerHttpResponse response
    ) {
        if (body == null) {
            writeJsonApiWithNoContent(response);
        }
        return body;
    }

    /**
     * 扩展 HTTP Message 转换器。
     * @param converters HTTP Message 转化器列表
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 添加用于将控制器方法的返回结果格式化为 JSON API 的 HTTP Message 转换器
        converters.add(0, new HttpMessageConverter() {
            /**
             * 取得当前转换器支持的媒体类型。
             * @return 媒体类型列表
             */
            @Override
            @NonNull
            public List<MediaType> getSupportedMediaTypes() {
                // 仅处理 JSON 数据
                return Collections.singletonList(APPLICATION_JSON);
            }

            /**
             * 判断是否使用当前转换器处理 HTTP 请求数据。
             * @param clazz     请求数据类型
             * @param mediaType 请求数据媒体类型
             * @return 是否读取 HTTP 请求数据
             */
            @Override
            public boolean canRead(@NonNull Class clazz, MediaType mediaType) {
                // 不处理 HTTP 请求数据
                return false;
            }

            /**
             * 处理 HTTP 请求数据。
             * 由于不处理 HTTP 请求数据，因此不会被执行。
             * @param clazz        请求数据类型
             * @param inputMessage HTTP 请求数据
             * @return 转换后的对象
             */
            @Override
            @NonNull
            public Object read(@NonNull Class clazz, @NonNull HttpInputMessage inputMessage) {
                return new Object();
            }

            /**
             * 判断是否使用当前转换器处理 HTTP 请求数据。
             * @param clazz     响应数据类型
             * @param mediaType 响应数据媒体类型
             * @return 是否写入 HTTP 响应数据
             */
            @Override
            public boolean canWrite(@NonNull Class clazz, MediaType mediaType) {
                // 取得 HTTP 请求实例
                HttpServletRequest request = RequestContextUtils.getRequest();

                // 仅处理 JSON 类型的返回数据
                if (request == null
                    || mediaType == null
                    || !APPLICATION_JSON_VALUE.equals(mediaType.toString())
                    || "springfox.documentation.spring.web.json.Json".equals(clazz.getName())) {
                    return false;
                }

                // 若控制器方法无返回值或返回值为 null 则生成默认数据结构
                if ("void".equals(clazz.getName())) {
                    writeJsonApiWithNoContent(RequestContextUtils.getResponse());
                    return false;
                }

                // 否则处理 HTTP 响应数据
                return true;
            }

            /**
             * 处理 HTTP 响应数据。
             * @param data         响应数据
             * @param contentType  媒体类型
             * @param response     HTTP 响应
             * @throws IOException I/O 异常
             */
            @Override
            public void write(@NonNull Object data, MediaType contentType, @NonNull HttpOutputMessage response) throws IOException {
                JsonApiDTO result = null;

                if (data instanceof JsonApiDTO) {
                    result = (JsonApiDTO) data;
                // 返回数据为数值时
                } else if (data instanceof Number) {
                    result = new JsonApiPrimitiveDTO<>((Number) data);
                // 返回数据为字符串时
                } else if (data instanceof String) {
                    result = new JsonApiPrimitiveDTO<>((String) data);
                // 返回数据为布尔时
                } else if (data instanceof Boolean) {
                    result = new JsonApiPrimitiveDTO<>((Boolean) data);
                // 返回数据为列表时
                } else if (data instanceof List) {
                    // noinspection unchecked
                    result = new JsonApiListDTO((List) data);
                // 返回数据为分页数据时
                } else if (data instanceof Page) {
                    // noinspection unchecked
                    result = new JsonApiListDTO((Page) data);
                // 返回数据为对象时
                } else if (data instanceof BaseDTO) {
                    result = new JsonApiObjectDTO<>((BaseDTO) data);
                }

                // 设置关联实体数据
                if (result instanceof JsonApiWithDataDTO) {
                    // noinspection unchecked, ConstantConditions
                    ((JsonApiWithDataDTO) result).setIncluded(
                        (Map<String, Object>) RequestContextUtils
                            .getRequest().getAttribute(REFERENCED_ENTITIES)
                    );
                }

                if (result == null) {
                    writeJsonApiWithNoContent(response);
                } else {
                    response
                        .getHeaders()
                        .put(CONTENT_TYPE, Collections.singletonList(contentType.toString()));
                    OutputStream responseBody = response.getBody();
                    responseBody.write(objectMapper.writeValueAsBytes(result));
                    responseBody.close();
                }
            }
        });
    }

    /**
     * 向 HTTP 响应输出空的 JSON API 对象数据。
     * @param response HTTP 响应
     */
    public void writeJsonApiWithNoContent(@Nullable HttpServletResponse response) {
        if (response == null || response.isCommitted()) {
            return;
        }
        response.setHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        try {
            writeJsonApiWithNoContent(response.getOutputStream(), response.getStatus());
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * 向 HTTP 响应输出空的 JSON API 对象数据。
     * @param response HTTP 响应
     */
    public void writeJsonApiWithNoContent(HttpOutputMessage response) {
        if (response == null) {
            return;
        }
        response.getHeaders().put(CONTENT_TYPE, Collections.singletonList(APPLICATION_JSON_VALUE));
        try {
            writeJsonApiWithNoContent(response.getBody(), ((ServletServerHttpResponse) response).getServletResponse().getStatus());
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * 向 HTTP 响应输出空的 JSON API 对象数据。
     * @param responseBody HTTP 响应的输出流
     */
    private void writeJsonApiWithNoContent(OutputStream responseBody, int status) {
        try {
            if (status < 400) {
                responseBody.write(successResponseBody);
            } else {
                JsonApiErrorDTO jsonApiErrorDTO = new JsonApiErrorDTO(status);
                JsonApiDTO jsonApiDTO = new JsonApiDTO();
                jsonApiDTO.setSuccess(false);
                jsonApiDTO.setError(jsonApiErrorDTO);
                responseBody.write(objectMapper.writeValueAsBytes(jsonApiDTO));
            }
            responseBody.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
