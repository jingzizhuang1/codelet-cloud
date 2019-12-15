package net.codelet.cloud.dto.verification;

/**
 * 手机号码验证数据传输对象。
 */
public interface SmsVerificationCodeDTO {

    String getMobile();

    String getSmsVerificationCode();
}
