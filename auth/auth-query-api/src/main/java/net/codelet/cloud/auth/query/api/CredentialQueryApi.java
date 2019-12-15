package net.codelet.cloud.auth.query.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "${services.auth.query.name:auth-query}",
    contextId = "credential-query"
)
public interface CredentialQueryApi {

    /**
     * 检查认证凭证是否可用。
     * @param credential 凭证
     * @return 认证凭证是否可用
     */
    @GetMapping("/credentials/{credential}/available")
    Boolean available(@PathVariable("credential") String credential);
}
