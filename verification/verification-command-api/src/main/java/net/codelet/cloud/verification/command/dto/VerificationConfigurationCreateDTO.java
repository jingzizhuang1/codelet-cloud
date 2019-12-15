package net.codelet.cloud.verification.command.dto;

import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.verification.vo.VerificationCharset;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;

/**
 * 消息模版创建事件附加数据。
 */
public class VerificationConfigurationCreateDTO extends VerificationConfigurationUpdateDTO {

    private static final long serialVersionUID = -3763696319515551934L;

    @Getter
    @Setter
    private VerificationType keyType;

    @Getter
    @Setter
    private VerificationPurpose purpose;

    public VerificationConfigurationCreateDTO() {
    }

    public VerificationConfigurationCreateDTO(
        VerificationType keyType,
        Integer codeLength,
        VerificationCharset codeCharset,
        Integer ttl,
        Integer intervalSeconds,
        Integer rateLimitPeriod,
        Integer rateLimitTimes
    ) {
        this.keyType = keyType;
        setCodeLength(codeLength);
        setCodeCharset(codeCharset);
        setTtl(ttl);
        setIntervalSeconds(intervalSeconds);
        setRateLimitPeriod(rateLimitPeriod);
        setRateLimitTimes(rateLimitTimes);
    }
}
