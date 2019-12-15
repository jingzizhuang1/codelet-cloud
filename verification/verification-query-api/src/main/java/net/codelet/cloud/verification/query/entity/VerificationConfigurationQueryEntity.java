package net.codelet.cloud.verification.query.entity;

import net.codelet.cloud.verification.entity.VerificationConfigurationBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "verification_configuration")
public class VerificationConfigurationQueryEntity extends VerificationConfigurationBaseEntity {
    private static final long serialVersionUID = 35509025471162L;
}
