package net.codelet.cloud.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Spring Boot 请求上下文工具。
 */
public class RequestContextUtils {

    /**
     * 取得保存在 Request 对象中的属性。
     * @param attributeName 属性名
     * @return 属性值
     */
    public static Object getAttributeValue(String attributeName) {
        HttpServletRequest request = getRequest();
        return request == null ? null : request.getAttribute(attributeName);
    }

    /**
     * 取得 HTTP 请求实例。
     * @return HTTP 请求实例
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }

    /**
     * 取得 HTTP 响应。
     * @return HTTP 响应
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = getRequestAttributes();
        return attributes == null ? null : attributes.getResponse();
    }

    /**
     * 取得基于 Servlet 实现的 HTTP 请求属性接口的实例。
     * @return 请求属性接口实例
     */
    private static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : ((ServletRequestAttributes) attributes);
    }
}
