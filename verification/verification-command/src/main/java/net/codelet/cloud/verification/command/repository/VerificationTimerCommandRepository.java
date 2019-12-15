package net.codelet.cloud.verification.command.repository;

import net.codelet.cloud.repository.BaseRepository;
import net.codelet.cloud.verification.command.entity.VerificationTimerCommandEntity;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;

import java.util.Optional;

/**
 * 验证码发送计时器数据仓库。
 */
public interface VerificationTimerCommandRepository extends BaseRepository<VerificationTimerCommandEntity> {

    /**
     * 取得验证码发送计时器。
     * @param keyType   验证类型
     * @param verifyKey 电子邮箱地址或手机号码等
     * @param purpose   验证码用途
     * @return 验证码发送计时器
     */
    Optional<VerificationTimerCommandEntity> findByKeyTypeAndVerifyKeyAndPurpose(VerificationType keyType, String verifyKey, VerificationPurpose purpose);
}
