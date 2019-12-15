package net.codelet.cloud.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpHeaders.*;

/**
 * 跨域请求配置：允许跨域请求。
 */
@Configuration
public class CorsConfiguration {

    /**
     * 当请求方法为 OPTIONS 时，设置允许跨域的响应头并结束。
     * @return WebFilter
     */
    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange context, WebFilterChain chain) -> {
            ServerHttpRequest request = context.getRequest();
            if (CorsUtils.isCorsRequest(request) && request.getMethod() == HttpMethod.OPTIONS) {
                HttpHeaders requestHeaders = request.getHeaders();
                ServerHttpResponse response = context.getResponse();
                HttpHeaders responseHeaders = response.getHeaders();
                putHeader(responseHeaders, ACCESS_CONTROL_ALLOW_CREDENTIALS, true, true);
                putHeader(responseHeaders, ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin(), "*");
                putHeader(responseHeaders, ACCESS_CONTROL_ALLOW_METHODS, requestHeaders.getAccessControlAllowMethods(), "*");
                putHeader(responseHeaders, ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlAllowHeaders(), "Authorization,X-Requested-With,Content-Type");
                putHeader(responseHeaders, ACCESS_CONTROL_EXPOSE_HEADERS, requestHeaders.getAccessControlExposeHeaders(), "*");
                putHeader(responseHeaders, ACCESS_CONTROL_MAX_AGE, requestHeaders.getAccessControlMaxAge(), 18000L);
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
            return chain.filter(context);
        };
    }

    /**
     * 设置响应头。
     * @param headers      响应的 HttpHeaders 实例
     * @param header       响应头
     * @param value        相应 CORS 响应头的值值
     * @param defaultValue 默认值
     */
    private static void putHeader(HttpHeaders headers, String header, Object value, Object defaultValue) {
        if (value == null
            || (value instanceof List && ((List) value).size() == 0)) {
            headers.put(header, Collections.singletonList(defaultValue.toString()));
            return;
        }
        if (value instanceof List) {
            // noinspection unchecked
            headers.put(header, (List) value);
        } else {
            headers.put(header, Collections.singletonList(value.toString()));
        }
    }
}
