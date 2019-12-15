package net.codelet.cloud.organization.query.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.organization.vo.DivisionType;
import net.codelet.cloud.organization.vo.OrganizationType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 组织层级结构查询条件数据传输对象。
 */
public class OrganizationHierarchyQueryDTO extends BaseDTO {

    private static final long serialVersionUID = 958659402034839132L;

    @Getter
    @Setter
    @Min(1)
    @Max(5)
    @ApiModelProperty("取得深度")
    private int depth = 2;

    @Getter
    @Setter
    @ApiModelProperty("组织类型")
    private OrganizationType type;

    @Getter
    @Setter
    @ApiModelProperty("组织结构类型（事业部类型）")
    private DivisionType divisionType;
}
