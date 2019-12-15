package net.codelet.cloud.verification.command.entity;

import net.codelet.cloud.verification.entity.VerificationTimerBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "verification_timer")
public class VerificationTimerCommandEntity extends VerificationTimerBaseEntity {
    private static final long serialVersionUID = 104934069909818L;
}
