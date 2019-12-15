package net.codelet.cloud.notification.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;

import javax.validation.constraints.*;

/**
 * 电子邮件配置更新表单。
 */
public class MailConfigurationUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = -2456856454977839252L;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 255)
    @ApiModelProperty("配置名称")
    private String name;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 255)
    @ApiModelProperty("主机名")
    private String host;

    @Getter
    @Setter
    @NotNull
    @Min(1)
    @Max(65535)
    @ApiModelProperty("端口号")
    private Integer port;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 45)
    @ApiModelProperty("协议")
    private String protocol;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 45)
    @ApiModelProperty("发送者名称")
    private String senderName;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 255)
    @ApiModelProperty("账号")
    private String username;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 255)
    @ApiModelProperty("密码")
    private String password;

    @Getter
    @Setter
    @NotNull
    @ApiModelProperty("是否启用 StartTLS")
    private Boolean startTlsEnabled;

    @Getter
    @Setter
    @NotNull
    @ApiModelProperty("是否必须使用 StartTLS")
    private Boolean startTlsRequired;

    @Getter
    @Setter
    @NotNull
    @Min(1)
    @ApiModelProperty("连接超时时长（秒）")
    private Integer connectionTimeout;

    @Getter
    @Setter
    @NotNull
    @Min(1)
    @ApiModelProperty("读取超时时长（秒）")
    private Integer readTimeout;

    @Getter
    @Setter
    @NotNull
    @Min(1)
    @ApiModelProperty("写入超时时长（秒）")
    private Integer writeTimeout;
}
