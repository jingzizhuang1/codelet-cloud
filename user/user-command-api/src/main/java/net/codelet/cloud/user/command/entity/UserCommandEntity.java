package net.codelet.cloud.user.command.entity;

import net.codelet.cloud.user.entity.UserProfileEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class UserCommandEntity extends UserProfileEntity {

    private static final long serialVersionUID = 15917230853249L;

}
