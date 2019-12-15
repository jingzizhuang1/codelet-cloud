package net.codelet.cloud.config;

import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.util.CryptoUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.Serializable;

/**
 * 应用安全配置。
 */
@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "application.security")
public class SecurityConfiguration implements Serializable {

    private static final long serialVersionUID = -3562686599314084257L;

    // 用户访问令牌 KEY
    @Getter
    @Value("${access-token-key:codelet-cloud}")
    private String accessTokenKey;

    // 用户访问令牌 TTL（秒）
    @Getter
    @Setter
    @Value("${access-token-ttl:1296000}")
    private long accessTokenTtl;

    // 用户访问令牌刷新频率（秒）
    @Getter
    @Setter
    @Value("${access-token-renew-frequency:86400}")
    private long accessTokenRenewFrequency;

    // 图形验证码 TTL（秒）
    @Getter
    @Setter
    @Value("${captcha-ttl:60}")
    private long captchaTtl;

    // 电子邮件验证码 TTL（秒）
    @Getter
    @Setter
    @Value("${email-verification-ttl:3600}")
    private long emailVerificationTtl;

    // 短信验证码 TTL（秒）
    @Getter
    @Setter
    @Value("${sms-verification-ttl:300}")
    private long smsVerificationTtl;

    public void setAccessTokenKey(String accessTokenKey) {
        this.accessTokenKey = CryptoUtils.md5(accessTokenKey);
    }

    /**
     * 取得用户访问令牌 TTL（毫秒）。
     * @return 用户访问令牌 TTL（毫秒）
     */
    public long getAccessTokenTtlMS() {
        return accessTokenTtl * 1000;
    }

    /**
     * 取得用户访问令牌刷新频率（毫秒）。
     * @return 用户访问令牌刷新频率（毫秒）
     */
    public long getAccessTokenRenewFrequencyMS() {
        return accessTokenRenewFrequency * 1000;
    }

    /**
     * 取得图形验证码 TTL（毫秒）。
     * @return 图形验证码 TTL（毫秒）
     */
    public long getCaptchaTtlMS() {
        return captchaTtl * 1000;
    }

    /**
     * 取得电子邮件验证码 TTL（毫秒）。
     * @return 电子邮件验证码 TTL（毫秒）
     */
    public long getEmailVerificationTtlMS() {
        return emailVerificationTtl * 1000;
    }

    /**
     * 取得短信验证码 TTL（毫秒）。
     * @return 短信验证码 TTL（毫秒）
     */
    public long getSmsVerificationTtlMS() {
        return smsVerificationTtl * 1000;
    }
}
