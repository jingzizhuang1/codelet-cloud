package net.codelet.cloud.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 切面工具。
 */
public class AspectUtils {

    /**
     * 取得切入点方法的参数的值。
     * @param point 切入点
     * @param names 参数名
     * @return 参数名称与值的映射表
     */
    public static ParameterHolder getParameters(JoinPoint point, String... names) {
        ParameterHolder result = new ParameterHolder();
        Set<String> nameSet = new HashSet<>(Arrays.asList(names));
        MethodSignature signature = (MethodSignature) point.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        String[] parameterNames = signature.getParameterNames();
        Object[] parameterValues = point.getArgs();
        Parameter parameter;
        String parameterName;
        Object parameterValue;

        for (int index = 0; index < parameters.length; index++) {
            parameter = parameters[index];
            parameterName = parameterNames[index];
            parameterValue = parameterValues[index];
            if (parameter.getAnnotation(PathVariable.class) == null
                || !(parameterValue instanceof String)) {
                continue;
            }
            if (nameSet.remove(parameterName)) {
                result.set(parameterName, parameterValue);
            }
        }

        return result;
    }

    /**
     * 取得指定类型的参数（仅取得第一个）。
     * @param point         切入点
     * @param parameterType 参数类型
     * @param <T>           参数类型范型
     * @return 参数
     */
    public static <T> T getParameter(JoinPoint point, Class<T> parameterType) {
        return getParameter(point, parameterType, null);
    }

    /**
     * 取得指定类型且拥有指定注解的参数（仅取得第一个）。
     * @param point          切入点
     * @param annotationType 注解类型
     * @return 参数
     */
    public static <T> T getParameter(JoinPoint point, Class<T> parameterType, Class<? extends Annotation> annotationType) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        Object[] parameterValues = point.getArgs();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object parameterValue = parameterValues[i];
            if (parameterType.isInstance(parameterValue)
                && (annotationType == null || parameter.getDeclaredAnnotation(annotationType) != null)) {
                // noinspection unchecked
                return (T) parameterValue;
            }
        }
        return null;
    }

    /**
     * 取得请求数据。
     * @param point 切入点
     * @return 请求数据对象
     */
    public static Object getRequestBody(JoinPoint point) {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        Object[] parameterValues = point.getArgs();
        Parameter parameter;

        for (int index = 0; index < parameters.length; index++) {
            parameter = parameters[index];
            if (parameter.getAnnotation(RequestBody.class) != null) {
                return parameterValues[index];
            }
        }

        return null;
    }

    /**
     * 被切入方法参数的容器。
     */
    public static class ParameterHolder {

        private final Map<String, Object> parameters = new HashMap<>();

        public ParameterHolder set(String name, Object value) {
            parameters.put(name, value);
            return this;
        }

        public Object get(String name) {
            return parameters.get(name);
        }

        public String getAsString(String name) {
            return get(name) == null ? null : (String) get(name);
        }
    }
}
