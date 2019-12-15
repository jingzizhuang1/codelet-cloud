package net.codelet.cloud.auth.command.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import net.codelet.cloud.auth.command.api.AuthenticationApi;
import net.codelet.cloud.auth.command.dto.AccessTokenDTO;
import net.codelet.cloud.auth.command.service.AccessTokenClaimService;
import net.codelet.cloud.config.SecurityConfiguration;
import net.codelet.cloud.dto.ContextDTO;
import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.error.AccessTokenExpiredError;
import net.codelet.cloud.error.AccessTokenInvalidError;
import net.codelet.cloud.error.UnauthorizedError;
import net.codelet.cloud.service.StringRedisService;
import net.codelet.cloud.util.BeanUtils;
import net.codelet.cloud.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.function.BiFunction;

/**
 * 访问令牌校验服务。
 */
public abstract class AccessTokenClaimServiceImpl extends StringRedisService implements AccessTokenClaimService {

    protected final String accessTokenSecretKey;

    private final AuthenticationApi authenticationApi;

    /**
     * 取得 Redis 中访问令牌所有者信息更新版本号 KEY。
     * @param accessTokenId 访问令牌 ID
     * @return Redis 中访问令牌所有者信息更新版本号 KEY
     */
    protected static String getAccessTokenOwnerRevisionKey(String accessTokenId) {
        return "access-token:" + accessTokenId + ":user-revision";
    }

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AccessTokenClaimServiceImpl(
        StringRedisTemplate stringRedisTemplate,
        SecurityConfiguration securityConfiguration,
        AuthenticationApi authenticationApi
    ) {
        super(stringRedisTemplate);
        accessTokenSecretKey = securityConfiguration.getAccessTokenKey();
        this.authenticationApi = authenticationApi;
    }

    /**
     * 校验用户访问令牌。
     * @param context     请求上下文对象
     * @param accessToken 访问令牌
     * @return 用户信息
     */
    @Override
    public OperatorDTO claim(
        final ContextDTO context,
        final String accessToken
    ) {
        return claim(context, accessToken, (contextDTO, accessTokenDTO) -> {
            if (needRenew(accessTokenDTO) && authenticationApi != null) {
                return authenticationApi.renew(accessTokenDTO.getUserId(), accessTokenDTO.getId());
            }
            return null;
        });
    }

    /**
     * 校验用户访问令牌。
     * @param context     请求上下文对象
     * @param accessToken 访问令牌
     * @param renew       访问令牌刷新处理逻辑
     * @return 用户信息
     */
    protected OperatorDTO claim(
        final ContextDTO context,
        final String accessToken,
        final BiFunction<ContextDTO, AccessTokenDTO, String> renew
    ) {
        if (StringUtils.isEmpty(accessToken, true)) {
            throw new UnauthorizedError();
        }

        try {
            // 校验 JWT，取得用户信息
            AccessTokenDTO accessTokenDTO = new AccessTokenDTO(
                Jwts
                    .parser()
                    .setSigningKey(accessTokenSecretKey + context.getClient())
                    .parseClaimsJws(accessToken)
                    .getBody()
                    .getSubject()
            );

            // 刷新访问令牌
            String renewedAccessToken = renew == null ? null : renew.apply(context, accessTokenDTO);

            // 设置请求上下文对象中的令牌所有者信息
            OperatorDTO operator = new OperatorDTO(accessTokenDTO.getUserId());
            operator.setAccessTokenId(accessTokenDTO.getId());
            operator.setRenewedAccessToken(renewedAccessToken);
            BeanUtils.copyProperties(accessTokenDTO, operator, "id");
            return operator;
        } catch (final ExpiredJwtException e) {
            throw new AccessTokenExpiredError();
        } catch (final JwtException e) {
            throw new AccessTokenInvalidError();
        }
    }

    /**
     * 判断是否需要刷新访问令牌。
     * 若在生成令牌后所有者用户信息被更新则需要刷新访问令牌。
     * @param accessTokenDTO 访问令牌数据
     * @return 是否需要刷新访问令牌
     */
    protected boolean needRenew(final AccessTokenDTO accessTokenDTO) {
        String revision = redisGet(getAccessTokenOwnerRevisionKey(accessTokenDTO.getId()));
        return StringUtils.isEmpty(revision) || Long.parseLong(revision) != accessTokenDTO.getUserRevision();
    }
}
