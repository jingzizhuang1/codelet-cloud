package net.codelet.cloud.verification.command.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.ValidateCaptcha;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.verification.command.api.VerificationCommandApi;
import net.codelet.cloud.verification.command.dto.VerificationCodeCreateDTO;
import net.codelet.cloud.verification.command.service.VerificationCommandService;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = {"domain=验证码", "biz=电子邮件及短信验证码", "responsibility=命令"})
public class VerificationCommandController extends BaseController implements VerificationCommandApi {

    private final VerificationCommandService verificationCommandService;

    @Autowired
    public VerificationCommandController(
        VerificationCommandService verificationCommandService
    ) {
        this.verificationCommandService = verificationCommandService;
    }

    @Override
    @ValidateCaptcha
    @ApiOperation(
        value = "发送电子邮箱验证邮件或手机号码验证短信",
        notes = "返回结果为可再次发送验证码的时间的 Unix 时间戳"
    )
    public Long sendVerificationCode(@Valid VerificationCodeCreateDTO createDTO) {
        return verificationCommandService
            .create(getContext(), createDTO.getKey(), createDTO.getPurpose());
    }

    @Override
    @ApiOperation("校验电子邮件/短信验证码")
    public void validate(
        @ApiParam("验证类型") VerificationType keyType,
        @ApiParam("电子邮件地址/手机号码等") String key,
        @ApiParam("验证码用途") VerificationPurpose purpose,
        @ApiParam("验证码") String code
    ) {
        verificationCommandService.validate(keyType, key, purpose, code);
    }

    @Override
    @ApiOperation("销毁验证码记录")
    public void delete(
        @ApiParam("验证类型") VerificationType keyType,
        @ApiParam("电子邮件地址/手机号码等") String key,
        @ApiParam("验证码用途") VerificationPurpose purpose,
        @ApiParam("验证码") String code
    ) {
        verificationCommandService.delete(keyType, key, purpose, code);
    }
}
