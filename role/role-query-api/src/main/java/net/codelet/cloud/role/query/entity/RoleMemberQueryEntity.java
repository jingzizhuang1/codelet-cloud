package net.codelet.cloud.role.query.entity;

import net.codelet.cloud.role.entity.RoleMemberBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "role_member")
public class RoleMemberQueryEntity extends RoleMemberBaseEntity {

    private static final long serialVersionUID = 133044938736464L;

}
