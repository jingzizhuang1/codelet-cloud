package net.codelet.cloud.organization.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.ReferenceEntity;
import net.codelet.cloud.entity.BaseVersionedEntity;
import net.codelet.cloud.organization.vo.DivisionType;
import net.codelet.cloud.organization.vo.OrganizationType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 组织。
 */
@MappedSuperclass
public class OrganizationBaseEntity extends BaseVersionedEntity {

    private static final long serialVersionUID = 144176311526127L;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("组织类型")
    private OrganizationType type;

    @Getter
    @Setter
    @ReferenceEntity("organization")
    @JsonProperty("company")
    @ApiModelProperty("所属公司 ID")
    private String companyId;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("事业部类型（仅适用于组织类型为 DIVISION 的组织）")
    private DivisionType divisionType;

    @Getter
    @Setter
    @ApiModelProperty("组织名称")
    private String name;

    @Getter
    @Setter
    @ApiModelProperty("组织名称拼音")
    private String namePinyin;

    @Getter
    @Setter
    @ApiModelProperty("组织描述")
    private String description;

    @Getter
    @Setter
    @ApiModelProperty("是否已被停用")
    private Boolean disabled = false;

    @Getter
    @Setter
    @ApiModelProperty("停用时间")
    private Date disabledAt;

    @Getter
    @Setter
    @ApiModelProperty("停用操作者用户 ID")
    private String disabledBy;
}
