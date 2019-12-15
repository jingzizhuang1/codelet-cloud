package net.codelet.cloud.auth.query.repository;

import net.codelet.cloud.auth.vo.CredentialType;
import net.codelet.cloud.repository.BaseRepository;
import net.codelet.cloud.auth.query.entity.CredentialQueryEntity;

/**
 * 认证凭证数据仓库。
 */
public interface CredentialQueryRepository extends BaseRepository<CredentialQueryEntity> {

    /**
     * 检查认证凭证是否存在。
     * @param type       认证凭证类型
     * @param credential 认证凭证
     * @return 检查结果
     */
    boolean existsByTypeAndCredentialAndDeletedIsFalse(CredentialType type, String credential);
}
