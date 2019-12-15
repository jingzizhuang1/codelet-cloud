package net.codelet.cloud.verification.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.verification.vo.VerificationCharset;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 验证码配置更新表单。
 */
public class VerificationConfigurationUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = -541945644504065241L;

    @Getter
    @Setter
    @NotNull
    @Min(4)
    @Max(16)
    @ApiModelProperty("验证码长度（不得小于 4 或大于 16）")
    private Integer codeLength;

    @Getter
    @Setter
    @NotNull
    @ApiModelProperty("验证码字符集（NUMBER：数字；HEXADECIMAL：十六进制值；ALPHABET_NUMBER：英文及字母）")
    private VerificationCharset codeCharset;

    @Getter
    @Setter
    @Min(30) // 最短：30秒
    @NotNull
    @ApiModelProperty(notes = "有效时长（秒）", required = true)
    private Integer ttl;

    @Getter
    @Setter
    @NotNull
    @Min(60) // 最短：一分钟
    @Max(86400) // 最长：一天
    @ApiModelProperty("发送间隔最小时长（秒）")
    private Integer intervalSeconds;

    @Getter
    @Setter
    @NotNull
    @Min(60) // 最短：一分钟
    @Max(604800) // 最长：一周
    @ApiModelProperty("发送频率限制周期（秒）")
    private Integer rateLimitPeriod;

    @Getter
    @Setter
    @NotNull
    @Min(1)
    @Max(100)
    @ApiModelProperty("发送频率限制次数")
    private Integer rateLimitTimes;

    @Getter
    @Setter
    @NotNull
    @Min(1)
    @Max(10)
    @ApiModelProperty("最大验证次数（默认：2）")
    private Integer maxVerifyTimes = 2;

    @Getter
    @Setter
    @NotBlank
    @ApiModelProperty(notes = "通知消息模版 ID", required = true)
    private String templateId;
}
