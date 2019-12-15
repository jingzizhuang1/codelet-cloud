package net.codelet.cloud.user.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.verification.EmailVerificationCodeDTO;
import net.codelet.cloud.dto.verification.SmsVerificationCodeDTO;

import javax.validation.constraints.Size;

public class UserProfileUpdateDTO extends UserUpdateDTO implements EmailVerificationCodeDTO, SmsVerificationCodeDTO {

    private static final long serialVersionUID = 4882800388950520734L;

    @Getter
    @Setter
    @Size(max = 64)
    @ApiModelProperty("电子邮件验证码")
    private String emailVerificationCode;

    @Getter
    @Setter
    @Size(min = 6, max = 6)
    @ApiModelProperty("短信验证码")
    private String smsVerificationCode;
}
