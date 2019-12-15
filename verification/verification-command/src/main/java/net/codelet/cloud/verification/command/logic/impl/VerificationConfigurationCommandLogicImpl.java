package net.codelet.cloud.verification.command.logic.impl;

import net.codelet.cloud.error.BusinessError;
import net.codelet.cloud.service.StringRedisService;
import net.codelet.cloud.verification.command.entity.VerificationConfigurationCommandEntity;
import net.codelet.cloud.verification.command.logic.VerificationConfigurationCommandLogic;
import net.codelet.cloud.verification.command.repository.VerificationConfigurationCommandRepository;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 验证码配置业务逻辑。
 */
@Component
public class VerificationConfigurationCommandLogicImpl extends StringRedisService implements VerificationConfigurationCommandLogic {

    /** 验证码配置在 Redis 中的 TTL（秒） */
    private static final int VERIFICATION_CONFIGURATION_REDIS_KEY_TTL = 60;

    private final VerificationConfigurationCommandRepository verificationConfigurationCommandRepository;

    @Autowired
    public VerificationConfigurationCommandLogicImpl(
        StringRedisTemplate stringRedisTemplate,
        VerificationConfigurationCommandRepository verificationConfigurationCommandRepository
    ) {
        super(stringRedisTemplate);
        this.verificationConfigurationCommandRepository = verificationConfigurationCommandRepository;
    }

    /**
     * 生成验证码配置在 Redis 中的 Key。
     * @param keyType KEY 类型
     * @param purpose 用途
     * @return 配置信息
     */
    @Override
    public String redisKey(
        VerificationType keyType,
        VerificationPurpose purpose
    ) {
        return "verification-configurations:" + keyType.name() + ":" + purpose.name();
    }

    /**
     * 取得验证码配置。
     * @param keyType KEY 类型
     * @param purpose 用途
     * @return 验证码配置信息
     */
    @Override
    public VerificationConfigurationCommandEntity get(
        VerificationType keyType,
        VerificationPurpose purpose
    ) {
        // 尝试从缓存中取得验证码配置
        final String configKey = redisKey(keyType, purpose);
        VerificationConfigurationCommandEntity configuration = redisGetObject(
            configKey,
            VerificationConfigurationCommandEntity.class,
            VERIFICATION_CONFIGURATION_REDIS_KEY_TTL
        );

        if (configuration != null) {
            return configuration;
        }

        // 若未能从缓存中取得验证码配置则从数据库中获取
        configuration = verificationConfigurationCommandRepository
            .findByKeyTypeAndPurposeAndDisabledIsFalseAndDeletedIsFalse(keyType, purpose)
            .orElse(null);

        if (configuration == null) {
            throw new BusinessError("error.verification.no-configuration"); // TODO: message
        }

        redisSetObject(configKey, configuration, VERIFICATION_CONFIGURATION_REDIS_KEY_TTL);
        return configuration;
    }
}
