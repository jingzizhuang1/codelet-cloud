package net.codelet.cloud.role.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.ReferenceEntity;
import net.codelet.cloud.entity.BaseVersionedEntity;
import net.codelet.cloud.vo.Permission;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class RolePrivilegeBaseEntity extends BaseVersionedEntity {

    private static final long serialVersionUID = 253910791973788L;

    @Getter
    @Setter
    @ReferenceEntity("organization")
    @JsonProperty("org")
    @ApiModelProperty("角色组所属组织 ID")
    private String orgId;

    @Getter
    @Setter
    @ReferenceEntity("role")
    @JsonProperty("role")
    @ApiModelProperty("角色组 ID")
    private String roleId;

    @Getter
    @Setter
    @ApiModelProperty("适用领域")
    private String scope;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("访问许可")
    private Permission permission;

    @JsonIgnore
    public String privilege() {
        return scope + ":" + permission.name();
    }
}
