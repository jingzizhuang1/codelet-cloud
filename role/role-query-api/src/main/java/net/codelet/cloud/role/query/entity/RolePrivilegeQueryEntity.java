package net.codelet.cloud.role.query.entity;

import net.codelet.cloud.role.entity.RolePrivilegeBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "role_privilege")
public class RolePrivilegeQueryEntity extends RolePrivilegeBaseEntity {

    private static final long serialVersionUID = 83711249220855L;

}
