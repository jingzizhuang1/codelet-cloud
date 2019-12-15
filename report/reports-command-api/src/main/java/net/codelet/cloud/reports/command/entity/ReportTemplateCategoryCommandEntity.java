package net.codelet.cloud.reports.command.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.entity.BaseVersionedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "report_template_category")
public class ReportTemplateCategoryCommandEntity extends BaseVersionedEntity {
    private static final long serialVersionUID = -530207513426752318L;

    @Getter
    @Setter
    @ApiModelProperty("报表模板分类名称")
    private String categoryName;

}
