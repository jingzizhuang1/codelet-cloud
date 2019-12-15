package net.codelet.cloud.auth.command.api;

import net.codelet.cloud.auth.command.dto.AuthenticateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "${services.auth.command.name:auth-command}",
    contextId = "authentication-command"
)
public interface AuthenticationApi {

    /**
     * 验证登录凭证，生成访问令牌。
     * @param authenticateDTO 登录凭证
     */
    @PostMapping("/authorizations")
    void authenticate(@RequestBody AuthenticateDTO authenticateDTO);

    /**
     * 刷新访问令牌。
     * @param userId        用户 ID
     * @param accessTokenId 访问令牌 ID
     */
    @PostMapping("/users/{userId}/access-tokens/{accessTokenId}/renew")
    String renew(
        @PathVariable("userId") String userId,
        @PathVariable("accessTokenId") String accessTokenId
    );

    /**
     * 销毁访问令牌。
     * @param accessToken 访问令牌
     */
    @DeleteMapping("/authorizations/{accessToken}")
    void destroy(@PathVariable("accessToken") String accessToken);
}
