package net.codelet.cloud.reports.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;

import java.io.Serializable;

public class ReportTemplateCategoryUpdateDTO extends BaseDTO implements Serializable{
    private static final long serialVersionUID = 4092404554245159125L;

    @Getter
    @Setter
    @ApiModelProperty("报表模板类别名称")
    private String categoryName;
}
