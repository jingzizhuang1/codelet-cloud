package net.codelet.cloud.dto;

import net.codelet.cloud.util.CaptchaUtils;

/**
 * 人机验证数据传输对象接口。
 */
public interface CaptchaValidateDTO {

    /**
     * 取得人机验证数据。
     * @return 人机验证数据。
     */
    CaptchaUtils.CaptchaDTO getCaptcha();
}
