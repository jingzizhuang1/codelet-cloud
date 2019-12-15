package net.codelet.cloud.verification.query.service.impl;

import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.verification.query.dto.VerificationConfigurationQueryDTO;
import net.codelet.cloud.verification.query.entity.VerificationConfigurationQueryEntity;
import net.codelet.cloud.verification.query.repository.VerificationConfigurationQueryRepository;
import net.codelet.cloud.verification.query.service.VerificationConfigurationQueryService;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * 验证码配置查询服务。
 */
@Component
public class VerificationConfigurationQueryServiceImpl implements VerificationConfigurationQueryService {

    private final VerificationConfigurationQueryRepository verificationConfigurationQueryRepository;

    @Autowired
    public VerificationConfigurationQueryServiceImpl(VerificationConfigurationQueryRepository verificationConfigurationQueryRepository) {
        this.verificationConfigurationQueryRepository = verificationConfigurationQueryRepository;
    }

    /**
     * 查询验证码配置。
     * @param queryDTO 查询条件
     * @return 验证码配置数据
     */
    @Override
    public Page<VerificationConfigurationQueryEntity> search(VerificationConfigurationQueryDTO queryDTO) {
        return verificationConfigurationQueryRepository.findAllByCriteria(queryDTO, queryDTO.pageable());
    }

    /**
     * 取得配置信息详细信息。
     * @param keyType 验证类型
     * @param purpose 验证码用途
     * @return 配置信息详细信息
     */
    @Override
    public VerificationConfigurationQueryEntity get(
        VerificationType keyType,
        VerificationPurpose purpose
    ) {
        VerificationConfigurationQueryEntity configurationEntity = verificationConfigurationQueryRepository
            .findByKeyTypeAndPurposeAndDeletedIsFalse(keyType, purpose)
            .orElse(null);
        if (configurationEntity == null) {
            throw new NotFoundError();
        }
        return configurationEntity;
    }
}
