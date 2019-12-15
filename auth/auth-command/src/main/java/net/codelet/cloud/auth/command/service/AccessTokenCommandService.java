package net.codelet.cloud.auth.command.service;

import net.codelet.cloud.dto.ContextDTO;
import net.codelet.cloud.user.entity.UserBaseEntity;

public interface AccessTokenCommandService extends AccessTokenClaimService {

    /**
     * 创建访问令牌。
     * @param context    请求上下文
     * @param userEntity 用户基本信息
     * @return 访问令牌
     */
    String create(
        ContextDTO context,
        UserBaseEntity userEntity
    );

    /**
     * 销毁用户令牌。
     * @param context    请求上下文
     * @param accessToken 访问令牌
     */
    void destroy(
        ContextDTO context,
        String accessToken
    );

    /**
     * 刷新用户令牌。
     * @param context       请求上下文对象
     * @param userId        令牌所有者用户 ID
     * @param accessTokenId 访问令牌 ID
     * @return 更新后的访问令牌
     */
    String renew(
        ContextDTO context,
        String userId,
        String accessTokenId
    );
}
