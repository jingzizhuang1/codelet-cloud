package net.codelet.cloud.verification.command.entity;

import net.codelet.cloud.verification.entity.VerificationBaseEntity;
import net.codelet.cloud.verification.vo.VerificationType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "verification")
public class VerificationCommandEntity extends VerificationBaseEntity {

    private static final long serialVersionUID = 74195568980091L;

}
