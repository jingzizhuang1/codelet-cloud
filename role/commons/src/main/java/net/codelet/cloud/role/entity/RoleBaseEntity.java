package net.codelet.cloud.role.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.ReferenceEntity;
import net.codelet.cloud.entity.BaseVersionedEntity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class RoleBaseEntity extends BaseVersionedEntity {

    private static final long serialVersionUID = 149724643124987L;

    @Getter
    @Setter
    @ReferenceEntity("organization")
    @JsonProperty("company")
    @ApiModelProperty("所属公司 ID")
    private String companyId;

    @Getter
    @Setter
    @ReferenceEntity("organization")
    @JsonProperty("org")
    @ApiModelProperty("所属组织 ID")
    private String orgId;

    @Getter
    @Setter
    @ApiModelProperty("名称")
    private String name;

    @Getter
    @Setter
    @ApiModelProperty("名称拼音")
    private String namePinyin;
}
