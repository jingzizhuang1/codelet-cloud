package net.codelet.cloud.auth.command.entity;

import net.codelet.cloud.auth.entity.AccessTokenBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "access_token")
public class AccessTokenCommandEntity extends AccessTokenBaseEntity {

    private static final long serialVersionUID = 961573509941L;

}
