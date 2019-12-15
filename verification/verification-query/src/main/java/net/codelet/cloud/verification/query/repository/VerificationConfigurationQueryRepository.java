package net.codelet.cloud.verification.query.repository;

import net.codelet.cloud.repository.BaseRepository;
import net.codelet.cloud.verification.query.entity.VerificationConfigurationQueryEntity;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;

import java.util.Optional;

/**
 * 验证码配置信息数据仓库。
 */
public interface VerificationConfigurationQueryRepository extends BaseRepository<VerificationConfigurationQueryEntity> {

    /**
     * 取得配置信息详细信息。
     * @param keyType 验证类型
     * @param purpose 验证码用途
     * @return 配置信息详细信息
     */
    Optional<VerificationConfigurationQueryEntity> findByKeyTypeAndPurposeAndDeletedIsFalse(VerificationType keyType, VerificationPurpose purpose);
}
