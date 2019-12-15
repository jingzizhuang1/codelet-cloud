package net.codelet.cloud.verification.command.logic;

import net.codelet.cloud.verification.command.entity.VerificationConfigurationCommandEntity;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;

/**
 * 验证码配置业务逻辑接口。
 */
public interface VerificationConfigurationCommandLogic {

    /**
     * 生成验证码配置在 Redis 中的 Key。
     * @param keyType KEY 类型
     * @param purpose 用途
     * @return 配置信息
     */
    String redisKey(
        VerificationType keyType,
        VerificationPurpose purpose
    );

    /**
     * 取得验证码配置。
     * @param keyType KEY 类型
     * @param purpose 用途
     * @return 验证码配置信息
     */
    VerificationConfigurationCommandEntity get(
        VerificationType keyType,
        VerificationPurpose purpose
    );
}
