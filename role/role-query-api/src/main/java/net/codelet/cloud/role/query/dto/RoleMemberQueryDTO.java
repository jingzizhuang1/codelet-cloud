package net.codelet.cloud.role.query.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.PaginationDTO;

/**
 * 角色组成员查询条件。
 */
public class RoleMemberQueryDTO extends PaginationDTO {

    private static final long serialVersionUID = -6955884099512316165L;

    @Getter
    @Setter
    @ApiModelProperty(hidden = true)
    private String orgId;

    @Getter
    @Setter
    @ApiModelProperty(hidden = true)
    private String roleId;
}
