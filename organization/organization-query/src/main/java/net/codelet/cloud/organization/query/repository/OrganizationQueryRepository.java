package net.codelet.cloud.organization.query.repository;

import net.codelet.cloud.organization.vo.OrganizationType;
import net.codelet.cloud.repository.BaseRepository;
import net.codelet.cloud.organization.query.entity.OrganizationQueryEntity;

import java.util.Optional;

/**
 * 组织数据仓库。
 */
public interface OrganizationQueryRepository extends BaseRepository<OrganizationQueryEntity> {

    /**
     * 检查组织是否存在。
     * @param id 组织 ID
     * @return 组织实体信息
     */
    Boolean existsByIdAndDeletedIsFalse(String id);

    /**
     * 检查组织是否存在。
     * @param id   组织 ID
     * @param type 组织类型
     * @return 组织实体信息
     */
    Boolean existsByIdAndTypeAndDeletedIsFalse(String id, OrganizationType type);

    /**
     * 取得组织信息。
     * @param id 组织 ID
     * @return 组织信息
     */
    Optional<OrganizationQueryEntity> findByIdAndDeletedIsFalse(String id);
}
