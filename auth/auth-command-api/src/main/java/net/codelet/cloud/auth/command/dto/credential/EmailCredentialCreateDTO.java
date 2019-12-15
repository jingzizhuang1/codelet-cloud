package net.codelet.cloud.auth.command.dto.credential;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.dto.verification.EmailVerificationCodeDTO;
import net.codelet.cloud.util.RegExpUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class EmailCredentialCreateDTO extends BaseDTO implements EmailVerificationCodeDTO {

    private static final long serialVersionUID = 2529079148064650275L;

    @Getter
    @Setter
    @NotBlank
    @Pattern(regexp = RegExpUtils.EMAIL)
    @ApiModelProperty("电子邮箱地址")
    private String email;

    @Getter
    @Setter
    @NotBlank
    @ApiModelProperty("电子邮件验证码")
    private String emailVerificationCode;
}
