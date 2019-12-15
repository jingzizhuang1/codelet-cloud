package net.codelet.cloud.auth.command.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.dto.verification.EmailVerificationCodeDTO;
import net.codelet.cloud.dto.verification.SmsVerificationCodeDTO;
import net.codelet.cloud.util.RegExpUtils;

import javax.validation.constraints.NotEmpty;

public class AuthenticateDTO extends BaseDTO implements EmailVerificationCodeDTO, SmsVerificationCodeDTO {

    private static final long serialVersionUID = -6723160266188751512L;

    @Getter
    @NotEmpty
    @ApiModelProperty("登录凭证，如电子邮箱地址、手机号码、登录用户名等")
    private String username;

    @Getter
    @Setter
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String email;

    @Getter
    @Setter
    @ApiModelProperty("电子邮件验证码")
    private String emailVerificationCode;

    @Getter
    @Setter
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String mobile;

    @Getter
    @Setter
    @ApiModelProperty("短信验证码")
    private String smsVerificationCode;

    @Getter
    @Setter
    @ApiModelProperty("登录密码")
    private String password;

    public void setUsername(String username) {
        this.username = username;
        if (RegExpUtils.isEmailAddress(username)) {
            setEmail(username);
        } else if (RegExpUtils.isMobileNo(username)) {
            setMobile(username);
        }
    }
}
