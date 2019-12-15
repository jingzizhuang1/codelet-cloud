package net.codelet.cloud.verification.command.repository;

import net.codelet.cloud.repository.BaseRepository;
import net.codelet.cloud.verification.command.entity.VerificationConfigurationCommandEntity;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;

import java.util.Optional;

/**
 * 验证码配置数据仓库。
 */
public interface VerificationConfigurationCommandRepository extends BaseRepository<VerificationConfigurationCommandEntity> {

    /**
     * 检查是否存在验证码配置信息。
     * @param keyType 验证码类型
     * @param purpose 验证码用途
     * @return 验证码配置信息是否存在
     */
    boolean existsByKeyTypeAndPurpose(
        VerificationType keyType,
        VerificationPurpose purpose
    );

    /**
     * 检查是否存在验证码配置信息。
     * @param keyType 验证码类型
     * @param purpose 验证码用途
     * @param deleted 是否已删除
     * @return 验证码配置信息是否存在
     */
    boolean existsByKeyTypeAndPurposeAndDeletedIs(
        VerificationType keyType,
        VerificationPurpose purpose,
        boolean deleted
    );

    /**
     * 取得验证码配置信息。
     * @param keyType 验证码类型
     * @param purpose 验证码用途
     * @return 验证码配置信息
     */
    Optional<VerificationConfigurationCommandEntity> findByKeyTypeAndPurposeAndDeletedIsFalse(
        VerificationType keyType,
        VerificationPurpose purpose
    );

    /**
     * 取得验证码配置信息。
     * @param keyType 验证码类型
     * @param purpose 验证码用途
     * @return 验证码配置信息
     */
    Optional<VerificationConfigurationCommandEntity> findByKeyTypeAndPurposeAndDisabledIsFalseAndDeletedIsFalse(
        VerificationType keyType,
        VerificationPurpose purpose
    );
}
