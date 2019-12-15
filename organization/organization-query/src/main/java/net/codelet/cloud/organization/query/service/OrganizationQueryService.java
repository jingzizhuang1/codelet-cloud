package net.codelet.cloud.organization.query.service;

import net.codelet.cloud.organization.query.dto.OrganizationHierarchyTreeDTO;
import net.codelet.cloud.organization.query.dto.OrganizationHierarchyQueryDTO;
import net.codelet.cloud.organization.query.entity.OrganizationQueryEntity;
import net.codelet.cloud.organization.vo.OrganizationType;

import java.util.List;
import java.util.Set;

/**
 * 组织查询服务接口。
 */
public interface OrganizationQueryService {

    /**
     * 检查组织是否存在。
     * @param orgId 组织 ID
     * @param type  组织类型
     * @return 组织是否存在
     */
    Boolean exists(String orgId, OrganizationType type);

    /**
     * 取得组织的层级结构。
     * @param orgId    组织 ID
     * @param queryDTO 查询条件
     * @return 组织层级结构
     */
    OrganizationHierarchyTreeDTO hierarchy(
        String orgId,
        OrganizationHierarchyQueryDTO queryDTO
    );

    /**
     * 取得组织信息。
     * @param orgId 组织 ID
     * @return 组织信息
     */
    OrganizationQueryEntity get(String orgId);

    /**
     * 批量取得组织信息。
     * @param entityIDs 实体 ID 集合
     * @return 组织信息列表
     */
    List<OrganizationQueryEntity> get(Set<String> entityIDs);
}
