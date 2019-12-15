package net.codelet.cloud.verification.command.entity;

import net.codelet.cloud.verification.entity.VerificationSumBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "verification_sum")
public class VerificationSumCommandEntity extends VerificationSumBaseEntity {
    private static final long serialVersionUID = -4886991166567853063L;
}
