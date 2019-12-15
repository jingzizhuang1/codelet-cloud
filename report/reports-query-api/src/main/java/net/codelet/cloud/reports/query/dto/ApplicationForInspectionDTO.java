package net.codelet.cloud.reports.query.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class ApplicationForInspectionDTO extends BaseReportDTO {

    private static final long serialVersionUID = -7455647576101134348L;

    @Getter
    @Setter
    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("单据编号")
    private String reportNo;

    @Getter
    @Setter
    @ApiModelProperty("申请人姓名")
    private String applicantName;

    @Getter
    @Setter
    @ApiModelProperty("申请人电话")
    private String applicantTel;

    @Getter
    @Setter
    @ApiModelProperty("申请日期")
    private Date applyingDate;

    @Getter
    @Setter
    @ApiModelProperty("检验地点")
    private String inspectionLocation;

    @Getter
    @Setter
    @ApiModelProperty("检验日期")
    private Date inspectionDate;

    @Getter
    @Setter
    @ApiModelProperty("检验内容")
    private String inspectionContents;

    @Getter
    @Setter
    @ApiModelProperty("施工单位")
    private String constructionUnit;

    @Getter
    @Setter
    @ApiModelProperty("施工班组")
    private String constructionTeam;

    @Getter
    @Setter
    @ApiModelProperty("检验主管姓名")
    private String firstInspectionSupervisor;

    @Getter
    @Setter
    @ApiModelProperty("主管检验日期")
    private Date firstInspectionDate;

    @Getter
    @Setter
    @ApiModelProperty("检验结论")
    private String inspectionConclusion;

    @Getter
    @Setter
    @ApiModelProperty("检验结论主管")
    private String conclusionSupervisor;

    @Getter
    @Setter
    @ApiModelProperty("检验结论日期")
    private Date conclusionDate;

    @Getter
    @Setter
    @ApiModelProperty("复检内容")
    private String reinspectionContents;

    @Getter
    @Setter
    @ApiModelProperty("复检主管姓名")
    private String reinspectionSupervisor;

    @Getter
    @Setter
    @ApiModelProperty("复检日期")
    private Date reinspectionDate;

}
