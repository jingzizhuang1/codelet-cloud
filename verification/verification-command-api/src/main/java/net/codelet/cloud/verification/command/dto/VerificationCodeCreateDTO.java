package net.codelet.cloud.verification.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.dto.CaptchaValidateDTO;
import net.codelet.cloud.util.CaptchaUtils;
import net.codelet.cloud.vo.VerificationPurpose;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 验证码创建表单。
 */
public class VerificationCodeCreateDTO extends BaseDTO implements CaptchaValidateDTO {

    private static final long serialVersionUID = -8873194237659420037L;

    @Getter
    @Setter
    @NotNull
    @NotEmpty
    @ApiModelProperty("KEY（电子邮箱地址或手机号码）")
    private String key;

    @Getter
    @Setter
    @NotNull
    @ApiModelProperty("验证码用途")
    private VerificationPurpose purpose;

    @Getter
    @ApiModelProperty("人机验证数据")
    private CaptchaUtils.CaptchaDTO captcha;
}
