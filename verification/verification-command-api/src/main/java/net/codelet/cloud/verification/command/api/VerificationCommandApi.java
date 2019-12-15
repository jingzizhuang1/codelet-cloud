package net.codelet.cloud.verification.command.api;

import net.codelet.cloud.verification.command.dto.VerificationCodeCreateDTO;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "${services.verification.command.name:verification-command}",
    contextId = "verification-command"
)
public interface VerificationCommandApi {

    /**
     * 发送验证码。
     * @param createDTO 验证信息
     */
    @PostMapping("/verifications")
    Long sendVerificationCode(@RequestBody VerificationCodeCreateDTO createDTO);

    /**
     * 校验电子邮件/短信验证码。
     * @param keyType 验证类型
     * @param key     电子邮箱地址或手机号码
     * @param purpose 验证码用途
     * @param code    验证码
     */
    @PostMapping("/verifications/{keyType}/{key}/{purpose}/{code}/validate")
    void validate(
        @PathVariable("keyType") VerificationType keyType,
        @PathVariable("key") String key,
        @PathVariable("purpose") VerificationPurpose purpose,
        @PathVariable("code") String code
    );

    /**
     * 销毁验证码。
     * @param keyType 验证类型
     * @param key     电子邮箱地址或手机号码
     * @param purpose 验证码用途
     */
    @DeleteMapping("/verifications/{keyType}/{key}/{purpose}")
    void delete(
        @PathVariable("keyType") VerificationType keyType,
        @PathVariable("key") String key,
        @PathVariable("purpose") VerificationPurpose purpose,
        @RequestParam("code") String code
    );
}
