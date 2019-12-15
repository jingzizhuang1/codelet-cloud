package net.codelet.cloud.user.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class UserCreateDTO extends UserUpdateDTO {

    private static final long serialVersionUID = -2496921274074921574L;

    @Getter
    @Setter
    @NotNull
    @ApiModelProperty("是否停用")
    private Boolean disabled = false;
}
