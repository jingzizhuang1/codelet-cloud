package net.codelet.cloud.role.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * 角色组创建表单。
 */
public class RoleCreateDTO extends RoleUpdateDTO {

    private static final long serialVersionUID = -217961023118366220L;

    @Getter
    @Setter
    @ApiModelProperty("成员的用户 ID 集合")
    private Set<String> memberIDs;
}
