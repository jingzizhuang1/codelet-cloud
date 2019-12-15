package net.codelet.cloud.role.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;

import javax.validation.constraints.NotBlank;

/**
 * 角色组更新表单。
 */
public class RoleUpdateDTO extends RolePrivilegePutDTO {

    private static final long serialVersionUID = 6484446230851176132L;

    @Getter
    @Setter
    @NotBlank
    @ApiModelProperty("名称")
    private String name;
}
