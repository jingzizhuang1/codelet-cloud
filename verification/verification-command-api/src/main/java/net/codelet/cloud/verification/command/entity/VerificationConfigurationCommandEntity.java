package net.codelet.cloud.verification.command.entity;

import net.codelet.cloud.verification.entity.VerificationConfigurationBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "verification_configuration")
public class VerificationConfigurationCommandEntity extends VerificationConfigurationBaseEntity {

    private static final long serialVersionUID = 6281800219119504832L;
}
