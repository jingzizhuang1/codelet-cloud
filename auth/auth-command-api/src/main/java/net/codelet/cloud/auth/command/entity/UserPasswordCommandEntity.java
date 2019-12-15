package net.codelet.cloud.auth.command.entity;

import net.codelet.cloud.auth.entity.UserPasswordBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_password")
public class UserPasswordCommandEntity extends UserPasswordBaseEntity {

    private static final long serialVersionUID = 571215219220766829L;

}
