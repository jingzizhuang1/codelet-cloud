package net.codelet.cloud.role.query.service.impl;

import net.codelet.cloud.role.query.dto.RoleQueryDTO;
import net.codelet.cloud.role.query.entity.RoleQueryEntity;
import net.codelet.cloud.role.query.entity.RoleWithPrivilegesQueryEntity;
import net.codelet.cloud.role.query.repository.RoleQueryRepository;
import net.codelet.cloud.role.query.repository.RoleWithPrivilegesQueryRepository;
import net.codelet.cloud.role.query.service.RoleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * 角色组服务。
 */
@Component
public class RoleQueryServiceImpl implements RoleQueryService {

    private final RoleQueryRepository roleRepository;
    private final RoleWithPrivilegesQueryRepository roleWithPrivilegesRepository;

    @Autowired
    public RoleQueryServiceImpl(
        RoleQueryRepository roleRepository,
        RoleWithPrivilegesQueryRepository roleWithPrivilegesRepository
    ) {
        this.roleRepository = roleRepository;
        this.roleWithPrivilegesRepository = roleWithPrivilegesRepository;
    }

    /**
     * 查询角色组列表。
     * @param orgId    组织 ID
     * @param queryDTO 查询条件
     * @return 成员列表
     */
    @Override
    public Page<RoleQueryEntity> roles(String orgId, RoleQueryDTO queryDTO) {
        queryDTO.setOrgId(orgId);
        return roleRepository.findAllByCriteria(queryDTO, queryDTO.pageable());
    }

    /**
     * 取得角色详细信息。
     * @param roleId   角色 ID
     * @param orgId    所属组织 ID
     * @param revision 修订版本号
     * @return 角色详细信息
     */
    @Override
    public RoleWithPrivilegesQueryEntity role(String roleId, String orgId, Long revision) {
        RoleQueryDTO queryDTO = new RoleQueryDTO();
        queryDTO.setId(roleId);
        queryDTO.setOrgId(orgId);
        return roleWithPrivilegesRepository.findOneByCriteria(queryDTO, revision).orElse(null);
    }
}
