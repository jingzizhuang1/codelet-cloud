package net.codelet.cloud.verification.command.api;

import net.codelet.cloud.verification.command.dto.VerificationConfigurationUpdateDTO;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "${services.verification.command.name:verification-command}",
    contextId = "verification-notification-command"
)
public interface VerificationConfigurationCommandApi {

    /**
     * 设置配置。
     * @param keyType   验证类型
     * @param purpose   验证码用途
     * @param revision  更新版本号
     * @param updateDTO 更新表单
     */
    @PutMapping("/verification-configurations/{keyType}/{purpose}")
    @ResponseStatus(HttpStatus.CREATED)
    void set(
        @PathVariable("keyType") VerificationType keyType,
        @PathVariable("purpose") VerificationPurpose purpose,
        @RequestParam(value = "revision", required = false) Long revision,
        @RequestBody VerificationConfigurationUpdateDTO updateDTO
    );

    /**
     * 停用配置。
     * @param keyType  验证类型
     * @param purpose  验证码用途
     * @param revision 更新版本号
     */
    @PostMapping("/verification-configurations/{keyType}/{purpose}/disable")
    void disable(
        @PathVariable("keyType") VerificationType keyType,
        @PathVariable("purpose") VerificationPurpose purpose,
        @RequestParam("revision") long revision
    );

    /**
     * 启用配置。
     * @param keyType  验证类型
     * @param purpose  验证码用途
     * @param revision 更新版本号
     */
    @PostMapping("/verification-configurations/{keyType}/{purpose}/enable")
    void enable(
        @PathVariable("keyType") VerificationType keyType,
        @PathVariable("purpose") VerificationPurpose purpose,
        @RequestParam("revision") long revision
    );

    /**
     * 删除配置。
     * @param keyType  验证类型
     * @param purpose  验证码用途
     * @param revision 更新版本号
     */
    @DeleteMapping("/verification-configurations/{keyType}/{purpose}")
    void delete(
        @PathVariable("keyType") VerificationType keyType,
        @PathVariable("purpose") VerificationPurpose purpose,
        @RequestParam("revision") long revision
    );
}
