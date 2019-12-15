package net.codelet.cloud.auth.command.api;

import net.codelet.cloud.annotation.InternalAccessOnly;
import net.codelet.cloud.auth.command.dto.CredentialsCreateDTO;
import net.codelet.cloud.auth.command.dto.UserPasswordResetDTO;
import net.codelet.cloud.auth.command.dto.UserPasswordUpdateDTO;
import net.codelet.cloud.auth.command.dto.credential.EmailCredentialCreateDTO;
import net.codelet.cloud.auth.command.dto.credential.MobileCredentialCreateDTO;
import net.codelet.cloud.auth.command.dto.credential.UsernameUpdateDTO;
import net.codelet.cloud.auth.command.entity.CredentialCommandEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "${services.auth.command.name:auth-command}",
    contextId = "credential-command"
)
public interface CredentialCommandApi {

    /**
     * 创建电子邮箱地址登录凭证。
     * @param userId 用户 ID
     */
    @PostMapping("/users/{userId}/emails")
    CredentialCommandEntity createEmail(
        @PathVariable("userId") String userId,
        @RequestBody  EmailCredentialCreateDTO createDTO
    );

    /**
     * 删除电子邮箱地址登录凭证。
     * @param userId 用户 ID
     */
    @DeleteMapping("/users/{userId}/emails/{email}")
    void deleteEmail(
        @PathVariable("userId") String userId,
        @PathVariable("email") String email
    );

    /**
     * 创建手机号码登录凭证。
     * @param userId 用户 ID
     */
    @PostMapping("/users/{userId}/mobiles")
    CredentialCommandEntity createMobile(
        @PathVariable("userId") String userId,
        @RequestBody MobileCredentialCreateDTO createDTO
    );

    /**
     * 删除手机号码登录凭证。
     * @param userId 用户 ID
     */
    @DeleteMapping("/users/{userId}/mobiles/{mobile}")
    void deleteMobile(
        @PathVariable("userId") String userId,
        @PathVariable("mobile") String mobile
    );

    /**
     * 设置登录用户名。
     * @param userId 用户 ID
     */
    @PutMapping("/users/{userId}/username")
    CredentialCommandEntity setUsername(
        @PathVariable("userId") String userId,
        @RequestBody UsernameUpdateDTO updateDTO
    );

    /**
     * 通过电子邮件验证码或手机号码验证码重置登录密码。
     * @param credential 登录凭证
     * @param resetDTO   密码重置表单数据
     */
    @PostMapping("/users/{credential}/reset-password")
    void resetPassword(
        @PathVariable("credential") String credential,
        @RequestBody UserPasswordResetDTO resetDTO
    );

    /**
     * 通过旧密码设置新密码。
     * @param userId    用户 ID
     * @param updateDTO 密码更新表单数据
     */
    @PutMapping("/users/{userId}/password")
    void updatePassword(
        @PathVariable("userId") String userId,
        @RequestBody UserPasswordUpdateDTO updateDTO
    );

    /**
     * 批量创建认证凭证。
     * @param userId    用户 ID
     * @param createDTO 凭证批量创建表单数据
     */
    @InternalAccessOnly
    @PostMapping("/users/{userId}/credentials")
    void createCredentials(
        @PathVariable("userId") String userId,
        @RequestBody CredentialsCreateDTO createDTO
    );
}
