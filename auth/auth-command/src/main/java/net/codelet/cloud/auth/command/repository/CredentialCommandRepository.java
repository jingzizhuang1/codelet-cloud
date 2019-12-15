package net.codelet.cloud.auth.command.repository;

import net.codelet.cloud.auth.command.entity.CredentialCommandEntity;
import net.codelet.cloud.auth.vo.CredentialType;
import net.codelet.cloud.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

/**
 * 用户登录认证凭证数据仓库。
 */
public interface CredentialCommandRepository extends BaseRepository<CredentialCommandEntity> {

    /**
     * 根据类型和凭证取得凭证数据。
     * @param type       凭证类型
     * @param credential 凭证
     * @return 凭证数据
     */
    Optional<CredentialCommandEntity> findByTypeAndCredentialAndDeletedIsFalse(CredentialType type, String credential);

    /**
     * 取得用户的指定类型的所有凭证。
     * @param userId 用户 ID
     * @param type   凭证类型
     * @return 凭证数据列表
     */
    List<CredentialCommandEntity> findByUserIdAndTypeAndDeletedIsFalse(String userId, CredentialType type);

    /**
     * 取得用户的登录认证凭证。
     * @param userId     用户 ID
     * @param type       凭证类型
     * @param credential 凭证
     * @return 凭证数据
     */
    Optional<CredentialCommandEntity> findByUserIdAndTypeAndCredentialAndDeletedIsFalse(String userId, CredentialType type, String credential);

}
