package net.codelet.cloud.reports.query.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;

/**
 * 报表查询DTO。
 */
public class ReportsExampleGenerateResultDTO extends BaseDTO {

    private static final long serialVersionUID = 525970986347610489L;

    @Getter
    @Setter
    @ApiModelProperty("生成的报表的url")
    private String reportFileName;
}
