package net.codelet.cloud.role.query.entity;

import net.codelet.cloud.role.entity.RoleBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class RoleQueryEntity extends RoleBaseEntity {

    private static final long serialVersionUID = 114326507628696L;

}
