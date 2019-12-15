package net.codelet.cloud.controller;

import net.codelet.cloud.constant.HttpRequestAttributes;
import net.codelet.cloud.dto.ContextDTO;
import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.util.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HTTP 上下文基类。
 */
public abstract class HttpContext {

    /**
     * 取得 HTTP 请求实例。
     * @return HTTP 请求实例
     */
    protected static HttpServletRequest getRequest() {
        return RequestContextUtils.getRequest();
    }

    /**
     * 取得 HTTP 响应。
     * @return HTTP 响应
     */
    protected static HttpServletResponse getResponse() {
        return RequestContextUtils.getResponse();
    }

    /**
     * 取得上下文对象，若尚未构造则构造新的上下文对象。
     * @return 上下文对象
     */
    protected static ContextDTO getContext() {

        HttpServletRequest request = getRequest();

        if (request == null) {
            return new ContextDTO();
        }

        ContextDTO context = (ContextDTO) request.getAttribute(HttpRequestAttributes.CONTEXT);

        if (context != null) {
            return context;
        }

        context = new ContextDTO(request, getResponse());
        request.setAttribute(HttpRequestAttributes.CONTEXT, context);
        return context;
    }

    /**
     * 取得登录用户信息。
     * @return 登录用户信息
     */
    protected static OperatorDTO getOperator() {
        return getContext().getOperator();
    }
}
