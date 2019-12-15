package net.codelet.cloud.role.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 角色组权限更新表单。
 */
public class RolePrivilegePutDTO extends BaseDTO {

    private static final long serialVersionUID = -7842475847882485144L;

    @Getter
    @Setter
    @NotNull
    @ApiModelProperty("权限列表")
    private Set<PrivilegeDTO> privileges;
}
