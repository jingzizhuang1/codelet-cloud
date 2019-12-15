package net.codelet.cloud.verification.command.service.impl;

import net.codelet.cloud.config.SecurityConfiguration;
import net.codelet.cloud.dto.ContextDTO;
import net.codelet.cloud.error.AccessTokenExpiredError;
import net.codelet.cloud.error.TooManyRequestsError;
import net.codelet.cloud.service.StringRedisService;
import net.codelet.cloud.util.CaptchaUtils;
import net.codelet.cloud.verification.command.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 图形验证码服务。
 */
@Component
@EnableConfigurationProperties(SecurityConfiguration.class)
public class CaptchaServiceImpl extends StringRedisService implements CaptchaService {

    // 图形验证码 TTL（毫秒）
    private final long captchaTTL;

    /**
     * 构造方法。
     */
    @Autowired
    public CaptchaServiceImpl(
        StringRedisTemplate stringRedisTemplate,
        SecurityConfiguration securityConfiguration
    ) {
        super(stringRedisTemplate);
        this.captchaTTL = securityConfiguration.getCaptchaTtlMS();
    }

    /**
     * 生成缓存 KEY。
     * @param encryptedData 图形验证码加密数据
     * @return 缓存 KEY
     */
    private static String captchaKey(String encryptedData) {
        return "captcha:" + encryptedData;
    }

    /**
     * 生成图形验证码。
     * @param context 请求上下文
     * @return 图形验证码信息
     */
    @Override
    public CaptchaUtils.CaptchaData generate(final ContextDTO context) {
        CaptchaUtils.CaptchaData captcha = CaptchaUtils.generateImageOfText(context, captchaTTL);
        redisSet(captchaKey(captcha.getEncryptedData()), "initial", (new Long(captchaTTL / 1000)).intValue());
        return captcha;
    }

    /**
     * 校验图形验证码。
     * @param context    请求上下文
     * @param captchaDTO 图形验证码信息
     */
    @Override
    public void verify(final ContextDTO context, final CaptchaUtils.CaptchaDTO captchaDTO) {
        verify(context, captchaDTO, false);
    }

    /**
     * 校验图形验证码。
     * @param context    请求上下文
     * @param captchaDTO 图形验证码信息
     * @param validate   是否为正式验证
     */
    @Override
    public void verify(
        final ContextDTO context,
        final CaptchaUtils.CaptchaDTO captchaDTO,
        final Boolean validate
    ) {
        final String key = captchaKey(captchaDTO.getEncryptedData());
        final String status = redisGet(key);

        if (status == null) {
            throw new AccessTokenExpiredError("error.captcha.expired");
        } else if (!validate && "verified".equals(status)) {
            throw new TooManyRequestsError("error.captcha.no-more-chance");
        }

        try {
            CaptchaUtils.verify(context, captchaDTO);
        } finally {
            if (validate) {
                redisDelete(key);
            } else {
                redisSet(key, "verified");
            }
        }
    }
}
