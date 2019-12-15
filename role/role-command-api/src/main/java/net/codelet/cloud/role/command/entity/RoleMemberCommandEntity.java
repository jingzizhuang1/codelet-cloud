package net.codelet.cloud.role.command.entity;

import net.codelet.cloud.role.entity.RoleMemberBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "role_member")
public class RoleMemberCommandEntity extends RoleMemberBaseEntity {

    private static final long serialVersionUID = 274908460767176L;

}
