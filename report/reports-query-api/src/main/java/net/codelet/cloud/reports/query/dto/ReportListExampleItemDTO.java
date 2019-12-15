package net.codelet.cloud.reports.query.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

public class ReportListExampleItemDTO extends BaseReportDTO {


    private static final long serialVersionUID = -3716551826537769756L;

    @Getter
    @Setter
    @ApiModelProperty("序号")
    private String no;

    @Getter
    @Setter
    @ApiModelProperty("版本号")
    private String versionNo;

    @Getter
    @Setter
    @ApiModelProperty("需求类型")
    private String requirementType;

    @Getter
    @Setter
    @ApiModelProperty("需求描述")
    private String requirementDescription;

    @Getter
    @Setter
    @ApiModelProperty("原因（原始需求）")
    private String requirementReason;

    @Getter
    @Setter
    @ApiModelProperty("备注")
    private String remarks;

}
