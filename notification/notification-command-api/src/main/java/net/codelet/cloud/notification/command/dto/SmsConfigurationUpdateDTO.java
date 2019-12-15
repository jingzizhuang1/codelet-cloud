package net.codelet.cloud.notification.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.notification.vo.SmsProvider;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;

/**
 * 短信配置更新表单。
 */
public class SmsConfigurationUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = -4026312894614263299L;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 255)
    @ApiModelProperty("配置名称")
    private String name;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("短信服务供应商")
    private SmsProvider provider;

    @Getter
    @Setter
    @ApiModelProperty("账号名或访问 KEY")
    private String username;

    @Getter
    @Setter
    @ApiModelProperty("账号密码或访问密钥")
    private String password;

    @Getter
    @Setter
    @ApiModelProperty("短信签名")
    private String signName;
}
