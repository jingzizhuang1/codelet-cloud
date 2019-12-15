package net.codelet.cloud.verification.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.entity.BaseEntity;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class VerificationTimerBaseEntity extends BaseEntity {

    private static final long serialVersionUID = 278713408262484L;

    @Getter
    @Setter
    @ApiModelProperty("类型")
    @Enumerated(EnumType.STRING)
    private VerificationType keyType;

    @Getter
    @Setter
    @ApiModelProperty("验证 KEY（电子邮箱地址、手机号码等）")
    private String verifyKey;

    @Getter
    @Setter
    @ApiModelProperty("用途")
    @Enumerated(EnumType.STRING)
    private VerificationPurpose purpose;

    @Getter
    @Setter
    @ApiModelProperty("创建时间（周期开始时间）")
    private Date createdAt;

    @Getter
    @Setter
    @ApiModelProperty("最后发送时间")
    private Date lastSentAt;

    @Getter
    @Setter
    @ApiModelProperty("周期内发送次数")
    private Integer times;
}
