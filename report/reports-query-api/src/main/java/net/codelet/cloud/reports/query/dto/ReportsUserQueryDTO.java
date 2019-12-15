package net.codelet.cloud.reports.query.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.PaginationDTO;

/**
 * 用户查询条件。
 */
public class ReportsUserQueryDTO extends PaginationDTO {

    private static final long serialVersionUID = 525970986347610489L;

    @Getter
    @Setter
    @ApiModelProperty("姓名")
    private String name;
}
