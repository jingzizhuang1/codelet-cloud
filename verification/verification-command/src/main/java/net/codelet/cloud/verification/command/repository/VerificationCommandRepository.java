package net.codelet.cloud.verification.command.repository;

import net.codelet.cloud.repository.BaseRepository;
import net.codelet.cloud.verification.command.entity.VerificationCommandEntity;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * 验证码数据仓库。
 */
public interface VerificationCommandRepository extends BaseRepository<VerificationCommandEntity> {

    /**
     * 更新验证次数。
     * @param id 验证码记录 ID
     */
    @Transactional
    @Modifying
    @Query("UPDATE VerificationCommandEntity v SET v.verifiedTimes = v.verifiedTimes + 1 WHERE v.id = :id")
    void increaseVerifyTimes(@Param("id") String id);

    /**
     * 删除验证码。
     * @param keyType   验证类型
     * @param verifyKey 电子邮箱地址/手机号码等
     * @param purpose   验证码用途
     */
    void deleteByKeyTypeAndVerifyKeyAndPurpose(VerificationType keyType, String verifyKey, VerificationPurpose purpose);
}
