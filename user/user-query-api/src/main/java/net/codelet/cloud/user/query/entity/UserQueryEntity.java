package net.codelet.cloud.user.query.entity;

import net.codelet.cloud.user.entity.UserBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class UserQueryEntity extends UserBaseEntity {

    private static final long serialVersionUID = 268793490967493L;

}
