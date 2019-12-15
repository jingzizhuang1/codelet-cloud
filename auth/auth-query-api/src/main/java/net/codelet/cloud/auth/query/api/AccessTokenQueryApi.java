package net.codelet.cloud.auth.query.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
    name = "${services.auth.query.name:auth-query}",
    contextId = "access-token-query"
)
public interface AccessTokenQueryApi {
}
