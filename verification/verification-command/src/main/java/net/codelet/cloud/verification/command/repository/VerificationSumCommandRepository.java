package net.codelet.cloud.verification.command.repository;

import net.codelet.cloud.repository.BaseRepository;
import net.codelet.cloud.verification.command.entity.VerificationSumCommandEntity;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;

import java.util.Optional;

/**
 * 验证码数据仓库。
 */
public interface VerificationSumCommandRepository extends BaseRepository<VerificationSumCommandEntity> {

    /**
     * 取得验证码数据。
     * @param keyType   验证类型
     * @param verifyKey 电子邮箱地址或手机号码
     * @param purpose   验证码用途
     * @return 验证码数据
     */
    Optional<VerificationSumCommandEntity> findByKeyTypeAndVerifyKeyAndPurpose(VerificationType keyType, String verifyKey, VerificationPurpose purpose);
}
