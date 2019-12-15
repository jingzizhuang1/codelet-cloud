package net.codelet.cloud.filter;

import net.codelet.cloud.util.StringUtils;
import net.codelet.cloud.constant.HttpResponseHeaders;
import net.codelet.cloud.error.AccessDeniedError;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.*;

/**
 * HTTP 跨域请求头设置过滤器。
 */
public class HttpAccessControlFilter implements Filter {

    private String exposeHeaders;

    /**
     * 初始化过滤器。
     * @param filterConfig 过滤器配置
     */
    @Override
    public void init(FilterConfig filterConfig) {

        Field[] responseHeaderNames = HttpResponseHeaders.class.getDeclaredFields();
        List<String> exposeHeaderList = new ArrayList<>();

        for (Field responseHeaderName : responseHeaderNames) {
            if (responseHeaderName.getType() != String.class) {
                continue;
            }
            try {
                exposeHeaderList.add((String) responseHeaderName.get(null));
            } catch (IllegalAccessException e) {
                // nothing to do
            }
        }

        exposeHeaders = String.join(",", exposeHeaderList);
    }

    /**
     * 设置响应头。
     * @param response HTTP 响应
     * @param headerName 响应头名称
     * @param headerValue 响应头值
     * @param headerDefaultValue 响应头默认值
     */
    private void setResponseHeader(
        final HttpServletResponse response,
        final String headerName,
        final String headerValue,
        final String headerDefaultValue
    ) {
        if (StringUtils.isEmpty(headerValue)) {
            response.setHeader(headerName, headerDefaultValue);
        } else {
            response.setHeader(headerName, headerValue);
        }
    }

    /**
     * 设置跨域请求头。
     * @param req 请求
     * @param res 响应
     * @param chain 过滤器链
     */
    @Override
    public void doFilter(
        ServletRequest req,
        ServletResponse res,
        FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        final String userAgent = request.getHeader(USER_AGENT);

        // 必须设置用户代理字符串信息
        if (userAgent == null) {
            throw new AccessDeniedError();
        }

        // 浏览器客户端时设置跨域响应头
        if (userAgent.matches("^(Mozilla|Opera)/\\d+(\\.\\d+)*(\\s.+)?$")) {
            setResponseHeader(
                response,
                ACCESS_CONTROL_ALLOW_ORIGIN,
                request.getHeader(ORIGIN),
                "*"
            );
            setResponseHeader(
                response,
                ACCESS_CONTROL_ALLOW_METHODS,
                request.getHeader(ACCESS_CONTROL_REQUEST_METHOD),
                "OPTIONS".equals(request.getMethod()) ? "*" : request.getMethod()
            );
            setResponseHeader(
                response,
                ACCESS_CONTROL_ALLOW_HEADERS,
                request.getHeader(ACCESS_CONTROL_REQUEST_HEADERS),
                "Authorization,X-Requested-With,Content-Type"
            );
            setResponseHeader(
                response,
                ACCESS_CONTROL_EXPOSE_HEADERS,
                null,
                exposeHeaders
            );
            if ("OPTIONS".equals(request.getMethod())) {
                response.setStatus(200);
            }
        }

        chain.doFilter(req, res);
    }

    /**
     * 销毁过滤器。
     */
    @Override
    public void destroy() {
    }
}
