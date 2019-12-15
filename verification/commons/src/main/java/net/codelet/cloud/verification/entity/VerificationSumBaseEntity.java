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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
public class VerificationSumBaseEntity extends BaseEntity {

    private static final long serialVersionUID = 5110527410552987762L;

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
    private String codes;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("验证目的")
    private VerificationPurpose purpose;

    @Getter
    @Setter
    @ApiModelProperty("已验证次数")
    private Integer verifiedTimes = 0;

    /**
     * 取得验证码代码的集合。
     * @return 验证码代码的集合
     */
    public Set<String> getCodeSet() {
        return new HashSet<>(Arrays.asList(codes.split(",")));
    }
}
