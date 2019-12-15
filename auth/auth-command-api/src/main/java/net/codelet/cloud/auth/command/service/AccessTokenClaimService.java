package net.codelet.cloud.auth.command.service;

import net.codelet.cloud.dto.ContextDTO;
import net.codelet.cloud.dto.OperatorDTO;

/**
 * 访问令牌校验服务接口。
 */
public interface AccessTokenClaimService {

    /**
     * 校验用户访问令牌。
     * @param context     请求上下文对象
     * @param accessToken 访问令牌
     * @return 用户信息
     */
    OperatorDTO claim(
        ContextDTO context,
        String accessToken
    );
}
