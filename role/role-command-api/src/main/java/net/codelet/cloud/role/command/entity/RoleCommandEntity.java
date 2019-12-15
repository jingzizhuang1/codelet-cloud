package net.codelet.cloud.role.command.entity;

import net.codelet.cloud.role.entity.RoleBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class RoleCommandEntity extends RoleBaseEntity {

    private static final long serialVersionUID = 230266654113107L;

}
