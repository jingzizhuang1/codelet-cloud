package net.codelet.cloud.auth.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.dto.verification.EmailVerificationCodeDTO;
import net.codelet.cloud.dto.verification.SmsVerificationCodeDTO;
import net.codelet.cloud.util.RegExpUtils;

import javax.validation.constraints.Pattern;

/**
 * 密码重置数据传输对象。
 */
public class UserPasswordResetDTO extends BaseDTO implements EmailVerificationCodeDTO, SmsVerificationCodeDTO {

    private static final long serialVersionUID = -4281828354530120808L;

    @Getter
    @Setter
    @Pattern(regexp = RegExpUtils.EMAIL)
    @ApiModelProperty(value = "电子邮箱地址", hidden = true)
    private String email;

    @Getter
    @Setter
    @ApiModelProperty("电子邮件验证码")
    private String emailVerificationCode;

    @Getter
    @Setter
    @Pattern(regexp = RegExpUtils.MOBILE)
    @ApiModelProperty(value = "手机号码", hidden = true)
    private String mobile;

    @Getter
    @Setter
    @ApiModelProperty("短信验证码")
    private String smsVerificationCode;

    @Getter
    @Setter
    @ApiModelProperty("密码")
    private String password;
}
