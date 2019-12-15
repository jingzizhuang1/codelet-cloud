package net.codelet.cloud.role.command.service.impl;

import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.error.ConflictError;
import net.codelet.cloud.error.DuplicatedError;
import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.organization.query.api.OrganizationQueryApi;
import net.codelet.cloud.organization.query.entity.OrganizationQueryEntity;
import net.codelet.cloud.role.command.dto.PrivilegeDTO;
import net.codelet.cloud.role.command.dto.RoleCreateDTO;
import net.codelet.cloud.role.command.dto.RoleUpdateDTO;
import net.codelet.cloud.role.command.entity.RoleCommandEntity;
import net.codelet.cloud.role.command.entity.RolePrivilegeCommandEntity;
import net.codelet.cloud.role.command.repository.RoleCommandRepository;
import net.codelet.cloud.role.command.repository.RolePrivilegeCommandRepository;
import net.codelet.cloud.role.command.service.RoleCommandService;
import net.codelet.cloud.util.BeanUtils;
import net.codelet.cloud.util.PinyinUtils;
import net.codelet.cloud.vo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 角色组服务。
 */
@Component
public class RoleCommandServiceImpl implements RoleCommandService {

    private final OrganizationQueryApi organizationQueryApi;
    private final RoleCommandRepository roleRepository;
    private final RolePrivilegeCommandRepository rolePrivilegeRepository;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RoleCommandServiceImpl(
        OrganizationQueryApi organizationQueryApi,
        RoleCommandRepository roleRepository,
        RolePrivilegeCommandRepository rolePrivilegeRepository
    ) {
        this.organizationQueryApi = organizationQueryApi;
        this.roleRepository = roleRepository;
        this.rolePrivilegeRepository = rolePrivilegeRepository;
    }

    /**
     * 创建角色组。
     * @param operator  操作者
     * @param orgId     所属组织 ID
     * @param createDTO 角色组信息
     * @return 角色组信息
     */
    @Override
    @Transactional
    public RoleCommandEntity create(OperatorDTO operator, String orgId, RoleCreateDTO createDTO) {
        // 取得所属组织信息
        OrganizationQueryEntity organizationEntity = organizationQueryApi.get(orgId);

        // 检查是否存在名称相同的角色组
        if (roleRepository
            .existsByOrgIdAndNameAndDeletedIsFalse(orgId, createDTO.getName())) {
            throw new DuplicatedError("error.role.duplicate-role-with-same-name");
        }

        String companyId = organizationEntity.getCompanyId();

        // 保存角色组信息
        RoleCommandEntity roleEntity = new RoleCommandEntity();
        BeanUtils.copyProperties(createDTO, roleEntity);
        roleEntity.setCompanyId(companyId);
        roleEntity.setOrgId(orgId);
        roleEntity.setNamePinyin(PinyinUtils.convert(roleEntity.getName()));
        roleEntity.setCreatedAt(new Date());
        roleEntity.setCreatedBy(operator.getId());
        roleEntity.updateRevision();
        roleEntity = roleRepository.save(roleEntity);

        // 分配权限
        assign(operator, roleEntity, createDTO.getPrivileges());

        return roleEntity;
    }

    /**
     * 更新角色组信息。
     * @param operator  操作者
     * @param orgId     所属组织 ID
     * @param roleId    角色组 ID
     * @param updateDTO 角色组信息
     * @param revision  修订版本号
     */
    @Override
    @Transactional
    public void update(OperatorDTO operator, String orgId, String roleId, RoleUpdateDTO updateDTO, Long revision) {
        // 取得角色组信息
        RoleCommandEntity roleEntity = get(roleId, orgId, revision);
        if (!BeanUtils.merge(updateDTO, roleEntity)) {
            return;
        }

        // 保存角色组信息
        roleEntity.setNamePinyin(PinyinUtils.convert(roleEntity.getName()));
        roleEntity.setLastModifiedAt(new Date());
        roleEntity.setLastModifiedBy(operator.getId());
        roleEntity.updateRevision();
        roleRepository.save(roleEntity);

        // 分配权限
        assign(operator, roleEntity, updateDTO.getPrivileges());
    }

    /**
     * 为角色分配权限。
     * @param operator      操作者
     * @param orgId         所属组织 ID
     * @param roleId        角色 ID
     * @param privilegeDTOs 权限列表
     */
    @Override
    public void assign(
        OperatorDTO operator,
        String orgId,
        String roleId,
        Collection<PrivilegeDTO> privilegeDTOs
    ) {
        assign(operator, get(roleId, orgId, null), privilegeDTOs);
    }

    /**
     * 为角色分配权限。
     * @param operator      操作者
     * @param roleEntity    角色
     * @param privilegeDTOs 权限列表
     */
    private void assign(
        OperatorDTO operator,
        RoleCommandEntity roleEntity,
        Collection<PrivilegeDTO> privilegeDTOs
    ) {
        if (privilegeDTOs == null) {
            return;
        }

        String roleId = roleEntity.getId();
        Map<String, RolePrivilegeCommandEntity> privilegeMap = new HashMap<>();

        // 取得已分配的权限
        rolePrivilegeRepository.findByRoleId(roleId).forEach(privilegeEntity ->
            privilegeMap.put(privilegeEntity.privilege(), privilegeEntity)
        );

        Date timestamp = new Date();
        String operatorId = operator.getId();
        Map<String, Map<Permission, RolePrivilegeCommandEntity>> scopes = new HashMap<>();

        privilegeDTOs.forEach(privilegeDTO -> {
            // 尝试从已分配的权限中取得权限分配记录
            RolePrivilegeCommandEntity privilegeEntity = privilegeMap
                .get(privilegeDTO.privilege());

            // 若权限已分配，且未被删除则忽略，否则将其更新为未删除状态
            if (privilegeEntity != null) {
                privilegeMap.remove(privilegeDTO.privilege());
                if (!privilegeEntity.getDeleted()) {
                    return;
                }
                privilegeEntity.setLastModifiedAt(timestamp);
                privilegeEntity.setLastModifiedBy(operatorId);
                privilegeEntity.setDeleted(false);
            // 若权限未分配则创建新的权限记录
            } else {
                privilegeEntity = new RolePrivilegeCommandEntity();
                privilegeEntity.setCompanyId(roleEntity.getCompanyId());
                privilegeEntity.setOrgId(roleEntity.getOrgId());
                privilegeEntity.setRoleId(roleId);
                privilegeEntity.setScope(privilegeDTO.getScope());
                privilegeEntity.setPermission(privilegeDTO.getPermission());
                privilegeEntity.setCreatedAt(timestamp);
                privilegeEntity.setCreatedBy(operatorId);
            }

            privilegeEntity.updateRevision();

            scopes
                .computeIfAbsent(privilegeEntity.getScope(), scope -> new HashMap<>())
                .put(privilegeEntity.getPermission(), privilegeEntity);
        });

        // 定义待保存权限分配记录列表
        List<RolePrivilegeCommandEntity> privilegeEntities = new ArrayList<>();

        // 若一个适用领域中存在全部许可的权限，则移除其他许可的权限
        scopes.forEach((scope, permissions) ->
            permissions.forEach((permission, privilegeEntity) -> {
                if (permissions.containsKey(Permission.ALL)
                    && privilegeEntity.getPermission() != Permission.ALL) {
                    if (privilegeEntity.getCreatedAt().compareTo(timestamp) >= 0) {
                        return;
                    }
                    privilegeEntity.setDeletedAt(timestamp);
                    privilegeEntity.setDeletedBy(operatorId);
                    privilegeEntity.setDeleted(true);
                    privilegeEntity.updateRevision();
                }
                privilegeEntities.add(privilegeEntity);
            })
        );

        // 删除其余权限
        privilegeMap.forEach((privilege, privilegeEntity) -> {
            if (privilegeEntity.getDeleted()) {
                return;
            }
            privilegeEntity.setDeletedAt(timestamp);
            privilegeEntity.setDeletedBy(operatorId);
            privilegeEntity.setDeleted(true);
            privilegeEntity.updateRevision();
            privilegeEntities.add(privilegeEntity);
        });

        // 若无权限分配记录需要保存则结束
        if (privilegeEntities.size() == 0) {
            return;
        }

        // 保存权限分配记录
        rolePrivilegeRepository.saveAll(privilegeEntities);
    }

    /**
     * 删除角色组。
     * @param operator 操作者信息
     * @param orgId    所属组织
     * @param roleId   角色组 ID
     * @param revision 修订版本号
     */
    @Override
    public void delete(OperatorDTO operator, String orgId, String roleId, Long revision) {
        RoleCommandEntity roleEntity = get(roleId, orgId, revision);
        roleEntity.setDeleted(true);
        roleEntity.setDeletedAt(new Date());
        roleEntity.setDeletedBy(operator.getId());
        roleRepository.save(roleEntity);
    }

    /**
     * 取得角色组信息。
     * @param id       角色组 ID
     * @param orgId    所属组织 ID
     * @param revision 修订版本号
     * @return 角色组信息
     */
    private RoleCommandEntity get(String id, String orgId, Long revision) {
        RoleCommandEntity roleEntity = roleRepository
            .findByOrgIdAndIdAndDeletedIsFalse(orgId, id).orElse(null);
        if (roleEntity == null) {
            throw new NotFoundError();
        }
        if (revision != null && !revision.equals(roleEntity.getRevision())) {
            throw new ConflictError();
        }
        return roleEntity;
    }
}
