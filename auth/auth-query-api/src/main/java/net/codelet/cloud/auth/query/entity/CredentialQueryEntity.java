package net.codelet.cloud.auth.query.entity;

import net.codelet.cloud.auth.entity.CredentialBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "credential")
public class CredentialQueryEntity extends CredentialBaseEntity {

    private static final long serialVersionUID = 124620734255244L;

}
