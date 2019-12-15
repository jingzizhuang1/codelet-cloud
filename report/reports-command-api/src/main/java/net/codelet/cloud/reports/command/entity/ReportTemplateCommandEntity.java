package net.codelet.cloud.reports.command.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.entity.BaseVersionedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "report_template")
public class ReportTemplateCommandEntity extends BaseVersionedEntity{
    private static final long serialVersionUID = 6389322560116199247L;

    @Getter
    @Setter
    @ApiModelProperty("所属报表模板分类ID")
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
