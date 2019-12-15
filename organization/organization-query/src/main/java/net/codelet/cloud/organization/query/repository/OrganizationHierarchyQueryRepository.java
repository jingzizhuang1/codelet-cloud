package net.codelet.cloud.organization.query.repository;

import net.codelet.cloud.repository.BaseRepository;
import net.codelet.cloud.organization.query.entity.OrganizationHierarchyQueryEntity;

import java.util.List;
import java.util.Set;

/**
 * 组织层级结构数据仓库。
 */
public interface OrganizationHierarchyQueryRepository extends BaseRepository<OrganizationHierarchyQueryEntity> {

    /**
     * 查询组织的下级组织层级关系数据。
     * @param parentIDs 上级组织 ID 的集合
     * @return 组织层级关系数据列表
     */
    List<OrganizationHierarchyQueryEntity> findByParentIdInAndDeletedIsFalse(Set<String> parentIDs);
}
