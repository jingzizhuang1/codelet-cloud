package net.codelet.cloud.auth.command.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.codelet.cloud.auth.command.dto.AccessTokenDTO;
import net.codelet.cloud.auth.command.entity.AccessTokenCommandEntity;
import net.codelet.cloud.auth.command.repository.AccessTokenCommandRepository;
import net.codelet.cloud.auth.command.service.AccessTokenCommandService;
import net.codelet.cloud.config.CacheConfiguration;
import net.codelet.cloud.config.SecurityConfiguration;
import net.codelet.cloud.dto.ContextDTO;
import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.error.AccessTokenExpiredError;
import net.codelet.cloud.error.AccessTokenInvalidError;
import net.codelet.cloud.user.entity.UserBaseEntity;
import net.codelet.cloud.user.query.api.UserQueryApi;
import net.codelet.cloud.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@EnableConfigurationProperties({CacheConfiguration.class, SecurityConfiguration.class})
public class AccessTokenCommandServiceImpl extends AccessTokenClaimServiceImpl implements AccessTokenCommandService {

    private final long accessTokenTTL;
    private final long accessTokenRenewFrequency;
    private final int accessTokenRenewedAtTTL;

    private final AccessTokenCommandRepository accessTokenCommandRepository;

    private final UserQueryApi userQueryApi;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AccessTokenCommandServiceImpl(
        StringRedisTemplate stringRedisTemplate,
        CacheConfiguration cacheConfiguration,
        SecurityConfiguration securityConfiguration,
        AccessTokenCommandRepository accessTokenCommandRepository,
        UserQueryApi userQueryApi
    ) {
        super(stringRedisTemplate, securityConfiguration, null);
        accessTokenTTL = securityConfiguration.getAccessTokenTtlMS();
        accessTokenRenewFrequency = securityConfiguration.getAccessTokenRenewFrequencyMS();
        accessTokenRenewedAtTTL = cacheConfiguration.getAccessTokenRenewedAtTTL();
        this.accessTokenCommandRepository = accessTokenCommandRepository;
        this.userQueryApi = userQueryApi;
    }

    /**
     * 创建访问令牌。
     * @param context    请求上下文
     * @param userEntity 用户基本信息
     * @return 访问令牌
     */
    @Override
    public String create(
        final ContextDTO context,
        final UserBaseEntity userEntity
    ) {
        final long now = System.currentTimeMillis();

        // 获取已生成的用户访问令牌
        // TODO: find by app ID
        AccessTokenCommandEntity accessTokenEntity = accessTokenCommandRepository
            .findByUserIdAndClient(userEntity.getId(), context.getClient())
            .orElse(null);

        // 若取得的用户访问令牌已过期则将其销毁
        if (accessTokenEntity != null && now > accessTokenEntity.getExpiresAt()) {
            destroy(accessTokenEntity.getId());
            accessTokenEntity = null;
        }

        // 若尚未创建过用户访问令牌则生成新的用户访问令牌
        if (accessTokenEntity == null) {
            OperatorDTO operator = context.getOperator();
            Date timestamp = new Date();
            accessTokenEntity = new AccessTokenCommandEntity();
            accessTokenEntity.setAppId(""); // TODO: set app ID ...
            accessTokenEntity.setUserId(userEntity.getId());
            accessTokenEntity.setScope(""); // TODO: set scope ...
            accessTokenEntity.setRemoteAddr(context.getRemoteAddr());
            accessTokenEntity.setClient(context.getClient());
            accessTokenEntity.setUserAgent(context.getUserAgent());
            accessTokenEntity.setUserRevision(userEntity.getRevision());
            accessTokenEntity.setCreatedAt(timestamp);
            accessTokenEntity.setCreatedBy(operator == null ? userEntity.getId() : operator.getId());
            accessTokenEntity.setExpiresAt(timestamp.getTime() + accessTokenTTL);
            accessTokenCommandRepository.save(accessTokenEntity);
        }

        // 设置用户信息
        AccessTokenDTO.UserDTO userDTO = new AccessTokenDTO.UserDTO();
        BeanUtils.copyProperties(userEntity, userDTO);

        // 生成用户访问令牌
        return Jwts
            .builder()
            .setSubject((new AccessTokenDTO(context, userDTO, accessTokenEntity)).toString())
            .signWith(SignatureAlgorithm.HS384, accessTokenSecretKey + context.getClient())
            .compact();
    }

    /**
     * 销毁用户令牌。
     * @param accessTokenId 用户访问令牌 ID
     */
    private void destroy(final String accessTokenId) {
        accessTokenCommandRepository.deleteById(accessTokenId);
        redisDelete(getAccessTokenOwnerRevisionKey(accessTokenId));
    }

    /**
     * 销毁用户令牌。
     * @param context    请求上下文
     * @param accessToken 访问令牌
     */
    @Override
    public void destroy(
        final ContextDTO context,
        final String accessToken
    ) {
        destroy(claim(context, accessToken).getAccessTokenId());
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
        return super.claim(context, accessToken, (contextDTO, accessTokenDTO) -> {
            if (needRenew(accessTokenDTO)) {
                return renew(contextDTO, accessTokenDTO.getUserId(), accessTokenDTO.getId());
            }
            return null;
        });
    }

    /**
     * 刷新用户令牌。
     * @param context       请求上下文对象
     * @param userId        令牌所有者用户 ID
     * @param accessTokenId 访问令牌 ID
     * @return 更新后的访问令牌
     */
    @Override
    public String renew(
        final ContextDTO context,
        final String userId,
        final String accessTokenId
    ) {
        final String accessTokenOwnerRevisionKey = getAccessTokenOwnerRevisionKey(accessTokenId);

        // 尝试从数据库中取得访问令牌信息
        AccessTokenCommandEntity accessTokenEntity = accessTokenCommandRepository
            .findByIdAndUserIdAndClient(accessTokenId, userId, context.getClient())
            .orElse(null);

        // 若令牌不存在则返回无效错误
        if (accessTokenEntity == null) {
            throw new AccessTokenInvalidError();
        }

        final long currentTimestamp = System.currentTimeMillis();
        final long expiresAt = accessTokenEntity.getExpiresAt();
        boolean renewed = false;

        // 若令牌已过期则返回错误
        if (currentTimestamp > expiresAt) {
            destroy(accessTokenEntity.getId());
            throw new AccessTokenExpiredError();
        }

        // 刷新访问令牌
        if ((accessTokenTTL - (expiresAt - currentTimestamp)) > accessTokenRenewFrequency
            || !context.getRemoteAddr().equals(accessTokenEntity.getRemoteAddr())
            || !context.getUserAgent().equals(accessTokenEntity.getUserAgent())
        ) {
            accessTokenEntity.setRemoteAddr(context.getRemoteAddr());
            accessTokenEntity.setUserAgent(context.getUserAgent());
            accessTokenEntity.setExpiresAt(currentTimestamp + accessTokenTTL);
            accessTokenCommandRepository.save(accessTokenEntity);
            renewed = true;
        }

        // 缓存用户令牌所有者信息的更新版本号
        redisSet(
            accessTokenOwnerRevisionKey,
            Long.toString(accessTokenEntity.getUserRevision()),
            accessTokenRenewedAtTTL
        );

        if (!renewed) {
            return null;
        }

        // 取得用户信息
        UserBaseEntity userEntity = userQueryApi.get(accessTokenEntity.getUserId());

        if (userEntity == null) {
            throw new AccessTokenInvalidError();
        }

        // 重新生成访问令牌
        return create(context, userEntity);
    }
}
