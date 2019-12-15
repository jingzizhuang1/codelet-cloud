package net.codelet.cloud.auth.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;

import javax.validation.constraints.NotEmpty;

public class UserPasswordUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = 8297950561824702316L;

    @Getter
    @Setter
    @ApiModelProperty("旧密码")
    private String oldPassword;

    @Getter
    @Setter
    @NotEmpty
    @ApiModelProperty("新密码")
    private String newPassword;
}
