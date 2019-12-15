package net.codelet.cloud.verification.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.entity.BaseVersionedEntity;
import net.codelet.cloud.verification.vo.VerificationCharset;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class VerificationConfigurationBaseEntity extends BaseVersionedEntity {

    private static final long serialVersionUID = 8771291093117319761L;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("类型")
    private VerificationType keyType;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("用途")
    private VerificationPurpose purpose;

    @Getter
    @Setter
    @ApiModelProperty("验证码长度")
    private Integer codeLength;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("验证码字符集（NUMBER：数字；HEXADECIMAL：十六进制值；ALPHABET_NUMBER：英文及字母）")
    private VerificationCharset codeCharset;

    @Getter
    @Setter
    @ApiModelProperty("有效时长（秒）")
    private Integer ttl;

    @Getter
    @Setter
    @ApiModelProperty("发送间隔最小时长（秒）")
    private Integer intervalSeconds;

    @Getter
    @Setter
    @ApiModelProperty("发送频率限制周期（秒）")
    private Integer rateLimitPeriod;

    @Getter
    @Setter
    @ApiModelProperty("发送频率限制次数")
    private Integer rateLimitTimes;

    @Getter
    @Setter
    @ApiModelProperty("最大验证次数")
    private Integer maxVerifyTimes = 2;

    @Getter
    @Setter
    @ApiModelProperty("通知消息模版 ID")
    private String templateId;

    @Getter
    @Setter
    @ApiModelProperty("是否已停用")
    private Boolean disabled = false;

    @Getter
    @Setter
    @ApiModelProperty("停用时间")
    private Date disabledAt;

    @Getter
    @Setter
    @ApiModelProperty("停用者 ID")
    private String disabledBy;

    public Long getIntervalMilliseconds() {
        return intervalSeconds * 1000L;
    }

    public Long getRateLimitPeriodMilliseconds() {
        return rateLimitPeriod * 1000L;
    }
}
