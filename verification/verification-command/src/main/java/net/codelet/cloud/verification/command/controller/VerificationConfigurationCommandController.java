package net.codelet.cloud.verification.command.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.verification.command.api.VerificationConfigurationCommandApi;
import net.codelet.cloud.verification.command.dto.VerificationConfigurationUpdateDTO;
import net.codelet.cloud.verification.command.service.VerificationConfigurationCommandService;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = {"domain=验证码", "biz=验证码配置", "responsibility=命令"})
public class VerificationConfigurationCommandController extends BaseController implements VerificationConfigurationCommandApi {

    private final VerificationConfigurationCommandService verificationConfigurationCommandService;

    @Autowired
    public VerificationConfigurationCommandController(
        VerificationConfigurationCommandService verificationConfigurationCommandService
    ) {
        this.verificationConfigurationCommandService = verificationConfigurationCommandService;
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("设置配置")
    public void set(
        @ApiParam("验证类型") VerificationType keyType,
        @ApiParam("验证码用途") VerificationPurpose purpose,
        @ApiParam("更新版本号") Long revision,
        @ApiParam("配置更新表单") @Valid VerificationConfigurationUpdateDTO updateDTO
    ) {
        if (verificationConfigurationCommandService.set(getOperator(), keyType, purpose, revision, updateDTO)) {
            getResponse().setStatus(HttpStatus.CREATED.value());
        }
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("停用配置")
    public void disable(
        @ApiParam("验证类型") VerificationType keyType,
        @ApiParam("验证码用途") VerificationPurpose purpose,
        @ApiParam("更新版本号") long revision
    ) {
        verificationConfigurationCommandService.disable(getOperator(), keyType, purpose, revision);
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("启用配置")
    public void enable(
        @ApiParam("验证类型") VerificationType keyType,
        @ApiParam("验证码用途") VerificationPurpose purpose,
        @ApiParam("更新版本号") long revision
    ) {
        verificationConfigurationCommandService.enable(getOperator(), keyType, purpose, revision);
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("删除配置")
    public void delete(
        @ApiParam("验证类型") VerificationType keyType,
        @ApiParam("验证码用途") VerificationPurpose purpose,
        @ApiParam("更新版本号") long revision
    ) {
        verificationConfigurationCommandService.delete(getOperator(), keyType, purpose, revision);
    }
}
