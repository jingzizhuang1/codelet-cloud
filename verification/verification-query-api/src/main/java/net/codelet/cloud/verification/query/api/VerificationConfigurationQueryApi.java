package net.codelet.cloud.verification.query.api;

import net.codelet.cloud.verification.query.dto.VerificationConfigurationQueryDTO;
import net.codelet.cloud.verification.query.entity.VerificationConfigurationQueryEntity;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "${services.verification.query.name:verification-query}",
    contextId = "verification-configuration-query"
)
public interface VerificationConfigurationQueryApi {

    /**
     * 查询验证码配置。
     * @param queryDTO 查询条件
     * @return 验证码配置数据
     */
    @GetMapping("/verification-configurations")
    Page<VerificationConfigurationQueryEntity> search(VerificationConfigurationQueryDTO queryDTO);

    /**
     * 取得验证码配置详细信息。
     * @param keyType 验证类型
     * @param purpose 验证码用途
     * @return 验证码配置详细信息
     */
    @GetMapping("/verification-configurations/{keyType}/{purpose}")
    VerificationConfigurationQueryEntity get(
        @PathVariable("keyType") VerificationType keyType,
        @PathVariable("purpose") VerificationPurpose purpose
    );
}
