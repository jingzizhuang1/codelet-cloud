package net.codelet.cloud.reports.query.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.reports.entity.ReportTemplatesBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "report_template_category")
public class ReportTemplateCategoryQueryEntity extends ReportTemplatesBaseEntity {

    private static final long serialVersionUID = 8014509207929960939L;

    @Getter
    @Setter
    @ApiModelProperty("模板类别名称")
    private String categoryName;

}
