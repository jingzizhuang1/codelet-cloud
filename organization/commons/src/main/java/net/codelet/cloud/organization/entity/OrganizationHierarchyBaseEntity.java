package net.codelet.cloud.organization.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.ReferenceEntity;
import net.codelet.cloud.entity.BaseVersionedEntity;
import net.codelet.cloud.organization.vo.DivisionType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

/**
 * 组织层级结构节点。
 */
@MappedSuperclass
public class OrganizationHierarchyBaseEntity extends BaseVersionedEntity {

    private static final long serialVersionUID = 229646396491517L;

    @Getter
    @Setter
    @ReferenceEntity("organization")
    @JsonProperty("org")
    @ApiModelProperty("组织 ID")
    private String orgId;

    @Getter
    @Setter
    @ReferenceEntity("organization")
    @JsonProperty("company")
    @ApiModelProperty("所属公司 ID")
    private String companyId;

    @Getter
    @Setter
    @ReferenceEntity("organization")
    @JsonProperty("division")
    @ApiModelProperty("所属事业部 ID")
    private String divisionId;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("所属事业部类型")
    private DivisionType divisionType;

    @Getter
    @Setter
    @ReferenceEntity("organization")
    @JsonProperty("parent")
    @ApiModelProperty("上级组织 ID")
    private String parentId;

    @Getter
    @Setter
    @ApiModelProperty("最大层级深度")
    private Integer depth;

    @Getter
    @Setter
    @ApiModelProperty("排序顺序")
    private Long sortOrder;
}
