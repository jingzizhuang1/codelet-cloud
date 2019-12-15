package net.codelet.cloud.dto.verification;

/**
 * 电子邮箱地址验证数据传输对象。
 */
public interface EmailVerificationCodeDTO {

    String getEmail();

    String getEmailVerificationCode();
}
