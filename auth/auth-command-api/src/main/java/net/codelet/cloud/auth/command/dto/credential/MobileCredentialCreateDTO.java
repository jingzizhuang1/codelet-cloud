package net.codelet.cloud.auth.command.dto.credential;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.dto.verification.SmsVerificationCodeDTO;
import net.codelet.cloud.util.RegExpUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class MobileCredentialCreateDTO extends BaseDTO implements SmsVerificationCodeDTO {

    private static final long serialVersionUID = -5120765167438320144L;

    @Getter
    @Setter
    @NotBlank
    @Pattern(regexp = RegExpUtils.MOBILE)
    @ApiModelProperty("手机号码")
    private String mobile;

    @Getter
    @Setter
    @NotBlank
    @ApiModelProperty("短信验证码")
    private String smsVerificationCode;
}
