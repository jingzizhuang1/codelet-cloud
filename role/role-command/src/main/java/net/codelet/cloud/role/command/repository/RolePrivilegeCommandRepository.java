package net.codelet.cloud.role.command.repository;

import net.codelet.cloud.repository.BaseRepository;
import net.codelet.cloud.role.command.entity.RolePrivilegeCommandEntity;

import java.util.List;

/**
 * 角色组权限数据仓库。
 */
public interface RolePrivilegeCommandRepository extends BaseRepository<RolePrivilegeCommandEntity> {

    /**
     * 取得角色组的包括已删除权限在内的所有权限。
     * @param roleId 角色组 ID
     * @return 权限列表
     */
    List<RolePrivilegeCommandEntity> findByRoleId(String roleId);
}
