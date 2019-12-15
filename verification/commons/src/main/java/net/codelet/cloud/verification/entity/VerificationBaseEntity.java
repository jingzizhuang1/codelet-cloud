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
public class VerificationBaseEntity extends BaseEntity {

    private static final long serialVersionUID = 109977728263203L;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("类型")
    private VerificationType keyType;

    @Getter
    @Setter
    @ApiModelProperty("KEY（如电子邮箱地址、手机号码）")
    private String verifyKey;

    @Getter
    @Setter
    @ApiModelProperty("代码")
    private String code;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("验证目的")
    private VerificationPurpose purpose;

    @Getter
    @Setter
    @ApiModelProperty("已验证次数")
    private Integer verifiedTimes = 0;

    @Getter
    @Setter
    @ApiModelProperty("创建时间")
    private Date createdAt = new Date();

    @Getter
    @Setter
    @ApiModelProperty("创建者用户 ID")
    private String createdBy;

    @Getter
    @Setter
    @ApiModelProperty("客户端远程 IP 地址")
    private String remoteAddr;

    @Getter
    @Setter
    @ApiModelProperty("用户代理字符串")
    private String userAgent;

    @Getter
    @Setter
    @ApiModelProperty("过期时间")
    private Date expiresAt;
}
