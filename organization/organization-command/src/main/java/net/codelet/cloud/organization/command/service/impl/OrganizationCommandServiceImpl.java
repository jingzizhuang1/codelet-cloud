package net.codelet.cloud.organization.command.service.impl;

import net.codelet.cloud.constant.EntityIDs;
import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.error.BusinessError;
import net.codelet.cloud.error.ConflictError;
import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.organization.command.dto.DivisionCreateDTO;
import net.codelet.cloud.organization.command.dto.OrganizationCreateDTO;
import net.codelet.cloud.organization.command.dto.OrganizationUpdateDTO;
import net.codelet.cloud.organization.command.entity.OrganizationCommandEntity;
import net.codelet.cloud.organization.command.entity.OrganizationHierarchyCommandEntity;
import net.codelet.cloud.organization.command.repository.OrganizationCommandRepository;
import net.codelet.cloud.organization.command.repository.OrganizationHierarchyCommandRepository;
import net.codelet.cloud.organization.command.service.OrganizationCommandService;
import net.codelet.cloud.organization.vo.OrganizationType;
import net.codelet.cloud.util.BeanUtils;
import net.codelet.cloud.util.PinyinUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 组织服务。
 */
@Component
public class OrganizationCommandServiceImpl implements OrganizationCommandService {

    // 最大层级深度
    private static final int MAX_DEPTH = 10;

    private final OrganizationCommandRepository organizationRepository;
    private final OrganizationHierarchyCommandRepository hierarchyRepository;

    @Autowired
    public OrganizationCommandServiceImpl(
        OrganizationCommandRepository organizationRepository,
        OrganizationHierarchyCommandRepository hierarchyRepository
    ) {
        this.organizationRepository = organizationRepository;
        this.hierarchyRepository = hierarchyRepository;
    }

    /**
     * 取得组织信息。
     * @param id 组织 ID
     * @return 组织信息
     */
    @Override
    public OrganizationCommandEntity get(String id) {
        OrganizationCommandEntity organizationEntity
            = organizationRepository.findByIdAndDeletedIsFalse(id).orElse(null);
        if (organizationEntity == null) {
            throw new NotFoundError();
        }
        return organizationEntity;
    }

    /**
     * 创建组织。
     * @param operator  操作者
     * @param parentId  上级组织 ID
     * @param type      组织类型
     * @param createDTO 组织信息
     * @param <T>       组织信息范型
     * @return 组织信息
     */
    @Override
    @Transactional
    public <T extends OrganizationCreateDTO> OrganizationCommandEntity create(
        OperatorDTO operator,
        String parentId,
        OrganizationType type,
        T createDTO
    ) {
        OrganizationCommandEntity parent = null;

        if (parentId != null) {
            parent = organizationRepository.findByIdAndDeletedIsFalse(parentId).orElse(null);
            if (parent == null) {
                throw new BusinessError("error.organization.no-such-parent"); // TODO: set message
            }
        }

        if (parent != null && !parent.getType().canBeParentOf(type)) {
            throw new BusinessError("error.organization.cannot-create-organization-of-type"); // TODO: set message
        }

        OrganizationCommandEntity organizationEntity = new OrganizationCommandEntity();
        BeanUtils.copyProperties(createDTO, organizationEntity);
        organizationEntity.setType(type);
        organizationEntity.setNamePinyin(PinyinUtils.convert(organizationEntity.getName()));
        organizationEntity.setCreatedAt(new Date());
        organizationEntity.setCreatedBy(operator.getId());
        organizationEntity.updateRevision();

        if (parent != null) {
            organizationEntity.setCompanyId(parent.getCompanyId());
        } else {
            organizationEntity.setCompanyId(organizationEntity.getId());
        }

        if (createDTO instanceof DivisionCreateDTO) {
            organizationEntity.setDivisionType(((DivisionCreateDTO) createDTO).getDivisionType());
        }

        organizationRepository.save(organizationEntity);
        addChildren(operator, parent, Collections.singleton(organizationEntity));

        return organizationEntity;
    }

    /**
     * 将组织放置到指定的上级下。
     * @param operator 操作者
     * @param parentId 目标上级组织 ID
     * @param orgIDs   操作对象组织 ID 的集合
     */
    @Override
    @Transactional
    public void put(OperatorDTO operator, String parentId, Set<String> orgIDs) {
        // 取得上级组织信息，若上级组织不存在则返回错误
        OrganizationCommandEntity parentEntity = organizationRepository
            .findByIdAndDeletedIsFalse(parentId)
            .orElse(null);
        if (parentEntity == null) {
            throw new NotFoundError();
        }

        // 取得已添加到指定的上级下的组织的 ID 的集合
        Set<String> childIDs = new HashSet<>();
        hierarchyRepository
            .findByParentIdAndOrgIdInAndDeletedIsFalse(parentId, orgIDs)
            .forEach(hierarchyEntity -> childIDs.add(hierarchyEntity.getOrgId()));

        // 若所有组织均已添加到指定的上级下则结束
        orgIDs.remove(parentId);
        orgIDs.removeAll(childIDs);
        if (orgIDs.size() == 0) {
            return;
        }

        // 取得所有组织的信息
        List<OrganizationCommandEntity> organizationEntities = organizationRepository
            .findByCompanyIdAndIdInAndDeletedIsFalse(parentEntity.getCompanyId(), orgIDs);

        // 若存在无效的组织 ID 则返回错误
        if (organizationEntities.size() < orgIDs.size()) {
            throw new BusinessError("error.organization.not-found"); // TODO: set message
        }

        // 将组织添加到指定的上级组织下
        addChildren(operator, parentEntity, organizationEntities);
    }

    /**
     * 添加下级组织。
     * @param operator 操作者信息
     * @param parent   上级组织
     * @param children 下级组织
     */
    private void addChildren(
        OperatorDTO operator,
        OrganizationCommandEntity parent,
        Collection<OrganizationCommandEntity> children
    ) {
        if (containsParentOf(children, parent)) {
            throw new BusinessError("error.organization.children-contains-parent"); // TODO: set message
        }

        final String parentId;
        List<OrganizationHierarchyCommandEntity> parentHierarchyEntities;
        List<OrganizationHierarchyCommandEntity> hierarchyEntities = new ArrayList<>();

        if (parent == null) {
            parentId = EntityIDs.ROOT_ID;
            parentHierarchyEntities = null;
        } else {
            parentId = parent.getId();
            parentHierarchyEntities = hierarchyRepository.findByOrgIdAndDeletedIsFalse(parentId);
        }

        children.forEach(child -> {
            OrganizationHierarchyCommandEntity hierarchyEntity = new OrganizationHierarchyCommandEntity();
            hierarchyEntity.setCompanyId(child.getCompanyId());
            hierarchyEntity.setParentId(parentId);
            hierarchyEntity.setOrgId(child.getId());
            hierarchyEntity.setCreatedAt(new Date());
            hierarchyEntity.setCreatedBy(operator.getId());
            hierarchyEntity.updateRevision();

            if (child.getType() == OrganizationType.DIVISION) {
                hierarchyEntity.setDivisionId(child.getId());
                hierarchyEntity.setDivisionType(child.getDivisionType());
            }

            // 公司时将上级节点设置为系统根节点
            if (parentHierarchyEntities == null) {
                hierarchyEntity.setDepth(0);
                hierarchyEntities.add(hierarchyEntity);
            // 否则取得上级组织的所有层级关系节点，并为每一个上级组织的层级关系节点下生成组织的层级关系节点
            } else {
                for (OrganizationHierarchyCommandEntity parentHierarchyEntity : parentHierarchyEntities) {
                    if (parentHierarchyEntity.getDepth() + 1 >= MAX_DEPTH) {
                        throw new BusinessError("error.organization.max-depth-reached"); // TODO: set message
                    }
                    OrganizationHierarchyCommandEntity childHierarchyEntity = new OrganizationHierarchyCommandEntity();
                    BeanUtils.copyProperties(hierarchyEntity, childHierarchyEntity, "id");
                    childHierarchyEntity.setDivisionId(
                        child.getDivisionType() != null
                            ? child.getId()
                            : parentHierarchyEntity.getDivisionId()
                    );
                    childHierarchyEntity.setDivisionType(
                        child.getDivisionType() != null
                            ? child.getDivisionType()
                            : parentHierarchyEntity.getDivisionType()
                    );
                    childHierarchyEntity.setDepth(parentHierarchyEntity.getDepth() + 1);
                    hierarchyEntities.add(childHierarchyEntity);
                }

            }
        });

        hierarchyRepository.saveAll(hierarchyEntities);
    }

    /**
     * 判断指定的组织列表中是否存在一个组织的上级组织。
     * @param orgs  可能的上级组织列表
     * @param child 下级组织
     * @return 判断结果
     */
    private Boolean containsParentOf(Collection<OrganizationCommandEntity> orgs, OrganizationCommandEntity child) {
        if (child == null) {
            return false;
        }
        Set<String> orgIDs = new HashSet<>();
        orgs.forEach(org -> orgIDs.add(org.getId()));
        return containsParentOf(orgIDs, child.getId());
    }

    /**
     * 判断指定的组织列表中是否存在一个组织的上级组织。
     * @param orgIDs  可能的上级组织列表
     * @param childId 下级组织 ID
     * @return 判断结果
     */
    private Boolean containsParentOf(Collection<String> orgIDs, String childId) {
        return containsParentOf(orgIDs, Collections.singleton(childId));
    }

    /**
     * 判断指定的组织列表中是否存在一个组织的上级组织。
     * @param orgIDs   可能的上级组织列表
     * @param childIDs 下级组织 ID 集合
     * @return 判断结果
     */
    private Boolean containsParentOf(Collection<String> orgIDs, Collection<String> childIDs) {
        if (childIDs.size() == 0) {
            return false;
        }
        Set<String> parentIDs = new HashSet<>();
        for (OrganizationHierarchyCommandEntity hierarchyEntity : hierarchyRepository.findByOrgIdInAndDeletedIsFalse(childIDs)) {
            if (orgIDs.contains(hierarchyEntity.getParentId())) {
                return true;
            }
            parentIDs.add(hierarchyEntity.getParentId());
        }
        return containsParentOf(orgIDs, parentIDs);
    }

    /**
     * 更新组织信息。
     * @param operator  操作者
     * @param parentId  上级组织 ID
     * @param orgId     组织 ID
     * @param revision  修订版本号
     * @param updateDTO 组织信息
     */
    @Override
    public void update(OperatorDTO operator, String parentId, String orgId, Long revision, OrganizationUpdateDTO updateDTO) {
        OrganizationCommandEntity organizationEntity = get(orgId, parentId, revision);

        if (!BeanUtils.merge(updateDTO, organizationEntity)) {
            return;
        }
        organizationEntity.setNamePinyin(PinyinUtils.convert(organizationEntity.getName()));
        organizationEntity.setLastModifiedAt(new Date());
        organizationEntity.setLastModifiedBy(operator.getId());
        organizationEntity.updateRevision();
        organizationRepository.save(organizationEntity);
    }

    /**
     * 停用组织。
     * @param operator 操作者
     * @param parentId 上级组织 ID
     * @param orgId    组织 ID
     * @param revision 修订版本号
     */
    @Override
    public void disable(OperatorDTO operator, String parentId, String orgId, Long revision) {
        OrganizationCommandEntity organizationEntity = get(orgId, parentId, revision);
        organizationEntity.setDisabled(true);
        organizationEntity.setLastModifiedAt(new Date());
        organizationEntity.setLastModifiedBy(operator.getId());
        organizationEntity.updateRevision();
        organizationRepository.save(organizationEntity);
    }

    /**
     * 启用组织。
     * @param operator 操作者
     * @param parentId 上级组织 ID
     * @param orgId    组织 ID
     * @param revision 修订版本号
     */
    @Override
    public void enable(OperatorDTO operator, String parentId, String orgId, Long revision) {
        OrganizationCommandEntity organizationEntity = get(orgId, parentId, revision);
        organizationEntity.setDisabled(false);
        organizationEntity.setLastModifiedAt(new Date());
        organizationEntity.setLastModifiedBy(operator.getId());
        organizationEntity.updateRevision();
        organizationRepository.save(organizationEntity);
    }

    /**
     * 取得组织。
     * @param orgId    组织 ID
     * @param parentId 上级组织 ID
     * @param revision 修订版本号
     * @return 组织信息
     */
    private OrganizationCommandEntity get(String orgId, String parentId, Long revision) {
        OrganizationHierarchyCommandEntity hierarchyEntity = hierarchyRepository
            .findByParentIdAndOrgIdAndDeletedIsFalse(parentId, orgId)
            .orElse(null);
        if (hierarchyEntity == null) {
            throw new NotFoundError();
        }
        OrganizationCommandEntity organizationEntity = organizationRepository
            .findByIdAndDeletedIsFalse(orgId).orElse(null);
        if (organizationEntity == null) {
            throw new NotFoundError();
        }
        if (revision != null && !revision.equals(organizationEntity.getRevision())) {
            throw new ConflictError();
        }
        return organizationEntity;
    }

    /**
     * 删除与下级组织的层级关系。
     * @param operator 操作者
     * @param parentId 上级组织 ID
     * @param childIDs 组织 ID 集合
     */
    @Override
    @Transactional
    public void deleteChildren(OperatorDTO operator, String parentId, Set<String> childIDs) {
        // 取得删除对象层级关系数据
        List<OrganizationHierarchyCommandEntity> children = hierarchyRepository
            .findByParentIdAndOrgIdInAndDeletedIsFalse(parentId, childIDs);
        if (children.size() == 0) {
            return;
        }

        final Date timestamp = new Date();

        // 将所有取得的层级关系数据标记为已删除
        children.forEach(child -> {
            child.setDeleted(true);
            child.setDeletedAt(timestamp);
            child.setDeletedBy(operator.getId());
            child.updateRevision();
        });
        hierarchyRepository.saveAll(children);
    }

    /**
     * 删除组织。
     * @param operator  操作者
     * @param companyId 公司 ID
     * @param orgId     组织 ID
     * @param revision  修订版本号
     */
    @Override
    @Transactional
    public void delete(OperatorDTO operator, String companyId, String orgId, Long revision) {
        // 取得删除对象组织信息
        OrganizationCommandEntity organizationEntity = organizationRepository
            .findByCompanyIdAndIdAndDeletedIsFalse(companyId, orgId)
            .orElse(null);
        if (organizationEntity == null) {
            throw new NotFoundError();
        }
        if (revision != null && !revision.equals(organizationEntity.getRevision())) {
            throw new ConflictError();
        }

        final Date timestamp = new Date();

        // 将组织标记为已删除
        organizationEntity.setDeleted(true);
        organizationEntity.setDeletedAt(timestamp);
        organizationEntity.setDeletedBy(operator.getId());
        organizationEntity.updateRevision();
        organizationRepository.save(organizationEntity);

        // 取得删除对象组织的层级关系数据
        List<OrganizationHierarchyCommandEntity> hierarchyEntities = new ArrayList<>();
        hierarchyEntities.addAll(hierarchyRepository.findByParentIdAndDeletedIsFalse(orgId));
        hierarchyEntities.addAll(hierarchyRepository.findByOrgIdAndDeletedIsFalse(orgId));
        if (hierarchyEntities.size() == 0) {
            return;
        }

        // 将组织的层级关系数据标记为已删除
        hierarchyEntities.forEach(hierarchyEntity -> {
            hierarchyEntity.setDeleted(true);
            hierarchyEntity.setDeletedAt(timestamp);
            hierarchyEntity.setDeletedBy(operator.getId());
            hierarchyEntity.updateRevision();
        });
        hierarchyRepository.saveAll(hierarchyEntities);
    }

    /**
     * 删除无上级组织的组织及其组织层级关系。
     * @param operator  操作者信息
     * @param companyId 公司 ID
     */
    @Override
    public void deleteOrganizationsWithNoParent(
        OperatorDTO operator,
        String companyId
    ) {
        (new Thread(() ->
            organizationRepository
                .deleteOrganizationsWithNoParent(operator.getId(), companyId))
        ).start();
    }
}
