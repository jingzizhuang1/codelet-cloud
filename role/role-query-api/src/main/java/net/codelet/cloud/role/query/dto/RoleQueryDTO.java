package net.codelet.cloud.role.query.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.PaginationDTO;

/**
 * 角色组查询条件。
 */
public class RoleQueryDTO extends PaginationDTO {

    private static final long serialVersionUID = 4648725005882555846L;

    @Getter
    @Setter
    @ApiModelProperty(hidden = true)
    private String id;

    @Getter
    @Setter
    @ApiModelProperty(hidden = true)
    private String orgId;
}
