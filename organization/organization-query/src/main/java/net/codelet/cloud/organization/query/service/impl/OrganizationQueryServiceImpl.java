package net.codelet.cloud.organization.query.service.impl;

import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.organization.query.dto.OrganizationHierarchyTreeDTO;
import net.codelet.cloud.organization.query.dto.OrganizationHierarchyQueryDTO;
import net.codelet.cloud.organization.query.entity.OrganizationQueryEntity;
import net.codelet.cloud.organization.query.repository.OrganizationHierarchyQueryRepository;
import net.codelet.cloud.organization.query.repository.OrganizationQueryRepository;
import net.codelet.cloud.organization.query.service.OrganizationQueryService;
import net.codelet.cloud.organization.vo.OrganizationType;
import net.codelet.cloud.service.StringRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 组织查询服务。
 */
@Component
public class OrganizationQueryServiceImpl extends StringRedisService implements OrganizationQueryService {

    private final OrganizationQueryRepository organizationRepository;
    private final OrganizationHierarchyQueryRepository hierarchyRepository;

    @Autowired
    public OrganizationQueryServiceImpl(
        StringRedisTemplate stringRedisTemplate,
        OrganizationQueryRepository organizationRepository,
        OrganizationHierarchyQueryRepository hierarchyRepository
    ) {
        super(stringRedisTemplate);
        this.organizationRepository = organizationRepository;
        this.hierarchyRepository = hierarchyRepository;
    }

    /**
     * 检查组织是否存在。
     * @param orgId 组织 ID
     * @param type  组织类型
     * @return 组织是否存在
     */
    @Override
    public Boolean exists(String orgId, OrganizationType type) {
        return type == null
            ? organizationRepository.existsByIdAndDeletedIsFalse(orgId)
            : organizationRepository.existsByIdAndTypeAndDeletedIsFalse(orgId, type);
    }

    /**
     * 取得组织的层级结构。
     * @param orgId    组织 ID
     * @param queryDTO 查询条件
     * @return 组织层级结构
     */
    @Override
    public OrganizationHierarchyTreeDTO hierarchy(
        String orgId,
        OrganizationHierarchyQueryDTO queryDTO
    ) {
        get(orgId);
        return hierarchyTree(
            hierarchyMap(Collections.singleton(orgId), new HashMap<>(), queryDTO.getDepth(), 0),
            new OrganizationHierarchyTreeDTO(orgId)
        );
    }

    /**
     * 设置上级组织 ID 与其下级组织 ID 集合的映射表。
     * @param parentIDs         上级组织 ID 的集合
     * @param parentChildrenMap 上级组织 ID 与其下级组织 ID 集合的映射表
     * @param maxDepth          取得的最大深度
     * @param depth             已取得的深度
     * @return 上级组织 ID 与其下级组织 ID 集合的映射表
     */
    private Map<String, Set<String>> hierarchyMap(
        Set<String> parentIDs,
        Map<String, Set<String>> parentChildrenMap,
        int maxDepth,
        int depth
    ) {
        if (parentIDs.size() == 0 || depth >= maxDepth) {
            return parentChildrenMap;
        }
        Set<String> childIDs = new HashSet<>();
        hierarchyRepository.findByParentIdInAndDeletedIsFalse(parentIDs).forEach(hierarchyNode -> {
            parentChildrenMap
                .computeIfAbsent(hierarchyNode.getParentId(), parentId -> new HashSet<>())
                .add(hierarchyNode.getOrgId());
            childIDs.add(hierarchyNode.getOrgId());
        });
        return hierarchyMap(childIDs, parentChildrenMap, maxDepth, depth + 1);
    }

    /**
     * 根据上级组织 ID 与其下级组织 ID 集合的映射表构造层级树。
     * @param parentChildrenMap 上级组织 ID 与其下级组织 ID 集合的映射表
     * @param hierarchyDTO      组织层级结构树
     * @return 组织层级结构树
     */
    private OrganizationHierarchyTreeDTO hierarchyTree(
        Map<String, Set<String>> parentChildrenMap,
        OrganizationHierarchyTreeDTO hierarchyDTO
    ) {
        Set<String> childIDs = parentChildrenMap.get(hierarchyDTO.getId());
        if (childIDs != null && childIDs.size() > 0) {
            childIDs.forEach(childId ->
                hierarchyDTO.addChild(
                    hierarchyTree(
                        parentChildrenMap,
                        new OrganizationHierarchyTreeDTO(childId)
                    )
                )
            );
        }
        return hierarchyDTO;
    }

    /**
     * 取得组织信息。
     * @param orgId 组织 ID
     * @return 组织信息
     */
    @Override
    public OrganizationQueryEntity get(String orgId) {
        OrganizationQueryEntity organizationEntity = organizationRepository
            .findByIdAndDeletedIsFalse(orgId)
            .orElse(null);
        if (organizationEntity == null) {
            throw new NotFoundError();
        }
        return organizationEntity;
    }

    /**
     * 批量取得组织信息。
     * @param entityIDs 实体 ID 集合
     * @return 组织信息列表
     */
    @Override
    public List<OrganizationQueryEntity> get(Set<String> entityIDs) {
        return organizationRepository.findByIdIn(entityIDs);
    }
}
