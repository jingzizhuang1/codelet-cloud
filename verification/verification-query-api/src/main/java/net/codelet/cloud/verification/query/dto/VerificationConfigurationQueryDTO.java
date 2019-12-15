package net.codelet.cloud.verification.query.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.PaginationDTO;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;

/**
 * 验证码配置查询参数。
 */
public class VerificationConfigurationQueryDTO extends PaginationDTO {

    @Getter
    @Setter
    @ApiModelProperty("验证码类型")
    private VerificationType keyType;

    @Getter
    @Setter
    @ApiModelProperty("验证码用途")
    private VerificationPurpose purpose;
}
