package net.codelet.cloud.role.command.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.ReferenceEntity;
import net.codelet.cloud.role.entity.RolePrivilegeBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "role_privilege")
public class RolePrivilegeCommandEntity extends RolePrivilegeBaseEntity {

    private static final long serialVersionUID = 137728149369670L;

    @Getter
    @Setter
    @ReferenceEntity("organization")
    @JsonProperty("company")
    @ApiModelProperty("公司 ID")
    private String companyId;
}
