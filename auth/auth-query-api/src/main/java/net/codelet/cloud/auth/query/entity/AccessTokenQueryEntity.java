package net.codelet.cloud.auth.query.entity;

import net.codelet.cloud.auth.entity.AccessTokenBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "access_token")
public class AccessTokenQueryEntity extends AccessTokenBaseEntity {

    private static final long serialVersionUID = 276099708591806L;

}
