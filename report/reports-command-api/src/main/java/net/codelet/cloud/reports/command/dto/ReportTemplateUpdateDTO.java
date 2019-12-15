package net.codelet.cloud.reports.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class ReportTemplateUpdateDTO extends BaseDTO implements Serializable{

    private static final long serialVersionUID = -2301884011215352166L;
    @Getter
    @Setter
    @ApiModelProperty("所属报表模板类别ID")
    private String categoryId;

    @Getter
    @Setter
    @ApiModelProperty("报表模板名称")
    private String reportTemplateName;

    @Getter
    @Setter
    @ApiModelProperty("填充报表模板的数据类型")
    private String dataType;

    @Getter
    @Setter
    @ApiModelProperty("报表模板的示例文件名称")
    private String exampleFileName;

    @Getter
    @Setter
    @ApiModelProperty("报表模板文件名称（jrxml文件）")
    private String templateFileName;

    @Getter
    @Setter
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @Getter
    @Setter
    @ApiModelProperty("报表模板的描述")
    private String description;
}
