package net.codelet.cloud.role.query.service;

import net.codelet.cloud.role.query.dto.RoleQueryDTO;
import net.codelet.cloud.role.query.entity.RoleQueryEntity;
import net.codelet.cloud.role.query.entity.RoleWithPrivilegesQueryEntity;
import org.springframework.data.domain.Page;

/**
 * 角色组服务接口。
 */
public interface RoleQueryService {

    /**
     * 查询角色组列表。
     * @param orgId    组织 ID
     * @param queryDTO 查询条件
     * @return 成员列表
     */
    Page<RoleQueryEntity> roles(String orgId, RoleQueryDTO queryDTO);

    /**
     * 取得角色详细信息。
     * @param roleId   角色 ID
     * @param orgId    所属组织 ID
     * @param revision 修订版本号
     * @return 角色详细信息
     */
    RoleWithPrivilegesQueryEntity role(String roleId, String orgId, Long revision);
}
