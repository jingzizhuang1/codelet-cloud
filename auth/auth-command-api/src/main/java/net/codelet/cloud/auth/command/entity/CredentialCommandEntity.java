package net.codelet.cloud.auth.command.entity;

import net.codelet.cloud.auth.entity.CredentialBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "credential")
public class CredentialCommandEntity extends CredentialBaseEntity {

    private static final long serialVersionUID = 84979729240017L;

}
