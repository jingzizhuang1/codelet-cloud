package net.codelet.cloud.verification.command.service;

import net.codelet.cloud.dto.ContextDTO;
import net.codelet.cloud.util.CaptchaUtils;

/**
 * 图形验证码服务。
 */
public interface CaptchaService {

    /**
     * 生成图形验证码。
     * @param context 请求上下文
     * @return 图形验证码信息
     */
    CaptchaUtils.CaptchaData generate(ContextDTO context);

    /**
     * 校验图形验证码。
     * @param context    请求上下文
     * @param captchaDTO 图形验证码信息
     */
    void verify(ContextDTO context, CaptchaUtils.CaptchaDTO captchaDTO);

    /**
     * 校验图形验证码。
     * @param context    请求上下文
     * @param captchaDTO 图形验证码信息
     * @param validate   是否为正式验证
     */
    void verify(ContextDTO context, CaptchaUtils.CaptchaDTO captchaDTO, Boolean validate);
}
