package net.codelet.cloud.role.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.ReferenceEntity;
import net.codelet.cloud.entity.BaseVersionedEntity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class RoleMemberBaseEntity extends BaseVersionedEntity {

    private static final long serialVersionUID = 280718201281506L;

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
    @ReferenceEntity("role")
    @JsonProperty("role")
    @ApiModelProperty("角色组 ID")
    private String roleId;

    @Getter
    @Setter
    @ReferenceEntity("employee")
    @JsonProperty("employee")
    @ApiModelProperty("成员职员 ID")
    private String employeeId;

    @Getter
    @Setter
    @ReferenceEntity("user")
    @JsonProperty("user")
    @ApiModelProperty("成员用户 ID")
    private String userId;
}
