package net.codelet.cloud.user.command.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.annotation.InternalAccessOnly;
import net.codelet.cloud.annotation.ValidateVerificationCode;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.error.ValidationError;
import net.codelet.cloud.user.command.api.UserCommandApi;
import net.codelet.cloud.user.command.dto.UserSignUpDTO;
import net.codelet.cloud.user.command.entity.UserCommandEntity;
import net.codelet.cloud.user.command.service.UserCommandService;
import net.codelet.cloud.util.RegExpUtils;
import net.codelet.cloud.vo.UserType;
import net.codelet.cloud.vo.VerificationPurpose;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = {"domain=用户", "biz=用户", "responsibility=命令"})
public class UserCommandController extends BaseController implements UserCommandApi {

    private final UserCommandService userCommandService;

    @Autowired
    public UserCommandController(
        UserCommandService userCommandService
    ) {
        this.userCommandService = userCommandService;
    }

    @Override
    @CheckUserPrivilege(required = false)
    @ValidateVerificationCode(VerificationPurpose.USER_SIGN_UP)
    @ApiOperation("用户注册")
    public void create(@Valid UserSignUpDTO signUpDTO) {
        userCommandService.create(getOperator(), UserType.USER, signUpDTO);
    }

    @Override
    @InternalAccessOnly
    @ApiOperation(
        value = "创建用户账号",
        notes = "该接口用于使用已经过验证的用户标识（电子邮箱地址、手机号码等）为用户创建新的账号，从而省略用户注册的操作。"
    )
    public UserCommandEntity create(@ApiParam("用户标识（电子邮箱地址、手机号码等）") String userId) {
        UserSignUpDTO signUpDTO = new UserSignUpDTO();

        // 用户标识为电子邮箱地址时
        if (RegExpUtils.isEmailAddress(userId)) {
            signUpDTO.setName(userId.split("@")[0]);
            signUpDTO.setEmail(userId);
        // 用户标识为手机号码时
        } else if (RegExpUtils.isMobileNo(userId)) {
            signUpDTO.setName(RegExpUtils.fakeMobileNo(userId));
            signUpDTO.setMobile(userId);
        // 用户标识无效，返回错误
        } else {
            throw new ValidationError("error.user.identity-is-invalid"); // TODO: set message
        }

        // 创建用户
        return userCommandService.create(getOperator(), UserType.USER, signUpDTO);
    }
}
