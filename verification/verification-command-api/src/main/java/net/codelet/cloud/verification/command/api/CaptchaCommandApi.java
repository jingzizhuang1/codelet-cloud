package net.codelet.cloud.verification.command.api;

import net.codelet.cloud.util.CaptchaUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "${services.verification.command.name:verification-command}",
    contextId = "captcha-command"
)
public interface CaptchaCommandApi {

    /**
     * 获取人机验证数据。
     */
    @GetMapping("/captcha")
    CaptchaUtils.CaptchaData get();

    /**
     * 校验人机验证识别结果。
     * @param captchaDTO 人机验证数据
     */
    @PostMapping("/validate-captcha")
    void validate(@RequestBody CaptchaUtils.CaptchaDTO captchaDTO);
}
