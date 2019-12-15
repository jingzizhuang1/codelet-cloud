package net.codelet.cloud.organization.command.repository;

import net.codelet.cloud.organization.command.entity.OrganizationHierarchyCommandEntity;
import net.codelet.cloud.repository.BaseRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 组织层级关系数据仓库。
 */
public interface OrganizationHierarchyCommandRepository extends BaseRepository<OrganizationHierarchyCommandEntity> {

    /**
     * 取得组织的所有层级关系数据。
     * @param parentId 上级组织 ID
     * @return 层级关系数据列表
     */
    List<OrganizationHierarchyCommandEntity> findByParentIdAndDeletedIsFalse(String parentId);

    /**
     * 取得组织的所有层级关系数据。
     * @param orgId 组织 ID
     * @return 层级关系数据列表
     */
    List<OrganizationHierarchyCommandEntity> findByOrgIdAndDeletedIsFalse(String orgId);

    /**
     * 取得所有组织的层级关系数据。
     * @param orgIDs 组织 ID 集合
     * @return 层级关系数据列表
     */
    List<OrganizationHierarchyCommandEntity> findByOrgIdInAndDeletedIsFalse(Collection<String> orgIDs);

    /**
     * 取得组织层级关系。
     * @param parentId 上级组织 ID
     * @param orgId    组织 ID
     * @return 组织层级关系
     */
    Optional<OrganizationHierarchyCommandEntity> findByParentIdAndOrgIdAndDeletedIsFalse(String parentId, String orgId);

    /**
     * 取得所有组织层级关系数据。
     * @param parentId 上级组织 ID
     * @param orgIDs   下级组织 ID
     * @return 层级关系数据列表
     */
    List<OrganizationHierarchyCommandEntity> findByParentIdAndOrgIdInAndDeletedIsFalse(String parentId, Collection<String> orgIDs);
}
