package net.codelet.cloud.verification.query.service;

import net.codelet.cloud.verification.query.dto.VerificationConfigurationQueryDTO;
import net.codelet.cloud.verification.query.entity.VerificationConfigurationQueryEntity;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;
import org.springframework.data.domain.Page;

/**
 * 验证码配置查询服务接口。
 */
public interface VerificationConfigurationQueryService {

    /**
     * 查询验证码配置。
     * @param queryDTO 查询条件
     * @return 验证码配置数据
     */
    Page<VerificationConfigurationQueryEntity> search(VerificationConfigurationQueryDTO queryDTO);

    /**
     * 取得配置信息详细信息。
     * @param keyType 验证类型
     * @param purpose 验证码用途
     * @return 配置信息详细信息
     */
    VerificationConfigurationQueryEntity get(
        VerificationType keyType,
        VerificationPurpose purpose
    );
}
