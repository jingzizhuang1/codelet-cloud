package net.codelet.cloud.verification.command.service;

import net.codelet.cloud.dto.ContextDTO;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;

/**
 * 验证码服务。
 */
public interface VerificationCommandService {

    /**
     * 创建验证码并发送电子邮件或短信。
     * @param context 请求上下文
     * @param key     电子邮箱地址或手机号码
     * @param purpose 验证码用途
     * @return 可再次发送验证码的 Unix 时间
     */
    Long create(ContextDTO context, String key, VerificationPurpose purpose);

    /**
     * 校验验证码。
     * @param keyType 验证类型
     * @param key     电子邮箱地址或手机号码
     * @param purpose 验证码用途
     * @param code    验证码
     */
    void validate(VerificationType keyType, String key, VerificationPurpose purpose, String code);

    /**
     * 销毁验证码。
     * @param keyType 验证类型
     * @param key     电子邮箱地址或手机号码
     * @param purpose 验证码用途
     * @param code    验证码
     */
    void delete(VerificationType keyType, String key, VerificationPurpose purpose, String code);
}
