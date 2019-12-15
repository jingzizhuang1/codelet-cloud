package net.codelet.cloud.role.query.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.role.entity.RoleBaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
public class RoleWithPrivilegesQueryEntity extends RoleBaseEntity {

    private static final long serialVersionUID = 7693826417229687134L;

    @Getter
    @Setter
    @OneToMany(mappedBy = "roleId", cascade = {CascadeType.ALL})
    @Where(clause = "deleted = 0")
    @ApiModelProperty("权限列表")
    private List<RolePrivilegeQueryEntity> privileges;
}
