package net.codelet.cloud.auth.command.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.annotation.InternalAccessOnly;
import net.codelet.cloud.auth.command.api.CredentialCommandApi;
import net.codelet.cloud.auth.command.dto.CredentialsCreateDTO;
import net.codelet.cloud.auth.command.dto.UserPasswordResetDTO;
import net.codelet.cloud.auth.command.dto.UserPasswordUpdateDTO;
import net.codelet.cloud.auth.command.dto.credential.EmailCredentialCreateDTO;
import net.codelet.cloud.auth.command.dto.credential.MobileCredentialCreateDTO;
import net.codelet.cloud.auth.command.dto.credential.UsernameUpdateDTO;
import net.codelet.cloud.auth.command.entity.CredentialCommandEntity;
import net.codelet.cloud.auth.command.service.CredentialCommandService;
import net.codelet.cloud.auth.vo.CredentialType;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.annotation.ValidateVerificationCode;
import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.error.NoPrivilegeError;
import net.codelet.cloud.util.RegExpUtils;
import net.codelet.cloud.vo.VerificationPurpose;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = {"domain=登录认证", "biz=认证凭证", "responsibility=命令"})
public class CredentialCommandController extends BaseController implements CredentialCommandApi {

    private final CredentialCommandService credentialCommandService;

    @Autowired
    public CredentialCommandController(
        CredentialCommandService credentialCommandService
    ) {
        this.credentialCommandService = credentialCommandService;
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("创建电子邮箱地址登录凭证")
    public CredentialCommandEntity createEmail(
        @ApiParam("用户 ID") String userId,
        @Valid EmailCredentialCreateDTO createDTO
    ) {
        return credentialCommandService
            .createCredential(getOperator(), userId, CredentialType.EMAIL, createDTO.getEmail());
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("删除电子邮箱地址登录凭证（电子邮箱解绑）")
    public void deleteEmail(
        @ApiParam("用户 ID") String userId,
        @ApiParam("电子邮箱地址") String email
    ) {
        credentialCommandService
            .deleteCredential(getOperator(), userId, CredentialType.EMAIL, email);
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("创建手机号码登录凭证")
    public CredentialCommandEntity createMobile(
        @ApiParam("用户 ID") String userId,
        @Valid MobileCredentialCreateDTO createDTO
    ) {
        return credentialCommandService
            .createCredential(getOperator(), userId, CredentialType.MOBILE, createDTO.getMobile());
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("删除手机号码登录凭证（手机号码解绑）")
    public void deleteMobile(
        @ApiParam("用户 ID") String userId,
        @ApiParam("手机号码") String mobile
    ) {
        credentialCommandService
            .deleteCredential(getOperator(), userId, CredentialType.MOBILE, mobile);
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("设置登录用户名")
    public CredentialCommandEntity setUsername(
        @ApiParam("用户 ID") String userId,
        @Valid UsernameUpdateDTO updateDTO
    ) {
        return credentialCommandService
            .setUsername(getOperator(), userId, updateDTO.getUsername());
    }

    @Override
    @CheckUserPrivilege(required = false)
    @ValidateVerificationCode(
        value = VerificationPurpose.RESET_PASSWORD,
        keyParameterNames = {"credential"}
    )
    @ApiOperation(
        value = "重置登录密码",
        notes = "通过用户 ID 重置密码时，操作者必须为系统管理员。"
    )
    public void resetPassword(
        @ApiParam("用户 ID、登录用户名、电子邮箱地址或手机号码等登录凭证") String credential,
        @Valid UserPasswordResetDTO resetDTO
    ) {
        OperatorDTO operator = getOperator();
        if (RegExpUtils.isID(credential) && (operator == null || !operator.isAdministrator())) {
            throw new NoPrivilegeError();
        }
        credentialCommandService
            .setPassword(operator, credential, resetDTO.getPassword());
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("通过旧密码设置新密码")
    public void updatePassword(
        @ApiParam("用户 ID") String userId,
        @Valid UserPasswordUpdateDTO updateDTO
    ) {
        credentialCommandService
            .setPassword(getOperator(), userId, updateDTO.getOldPassword(), updateDTO.getNewPassword());
    }

    @Override
    @InternalAccessOnly
    @ApiOperation("批量创建认证凭证")
    public void createCredentials(
        @ApiParam("用户 ID") String userId,
        @Valid CredentialsCreateDTO createDTO
    ) {
        credentialCommandService.createCredentials(null, userId, createDTO);
    }
}
