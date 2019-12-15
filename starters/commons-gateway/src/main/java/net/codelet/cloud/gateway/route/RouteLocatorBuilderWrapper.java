package net.codelet.cloud.gateway.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RouteLocatorBuilder 包装器。
 */
public class RouteLocatorBuilderWrapper {

    // 路由断言格式
    private final Pattern PREDICATE_PATTERN = Pattern.compile("^(OPTIONS|GET|HEAD|POST|PUT|PATCH|DELETE)\\s+(/.+)$");

    // 路由
    private final RouteLocatorBuilder.Builder routes;

    /**
     * 构造方法。
     * @param builder RouteLocatorBuilder
     */
    public RouteLocatorBuilderWrapper(RouteLocatorBuilder builder) {
        routes = builder.routes();
    }

    /**
     * 设置路由。
     * @param serviceName 目标服务名称
     * @param predicates  路由断言（请求方法 请求路径）
     * @return RouteLocatorBuilderWrapper
     */
    public RouteLocatorBuilderWrapper route(String serviceName, String... predicates) {
        for (String predicateString : predicates) {
            final Matcher matcher = PREDICATE_PATTERN.matcher(predicateString);
            if (!matcher.matches()) {
                throw new RuntimeException(String.format("Invalid predicate: %s", predicateString));
            }
            routes.route(predicate -> predicate
                .method(matcher.group(1))
                .and()
                .path(matcher.group(2))
                .uri("lb://" + serviceName)
            );
        }
        return this;
    }

    /**
     * 编译路由。
     * @return RouteLocator
     */
    public RouteLocator build() {
        return routes.build();
    }
}
