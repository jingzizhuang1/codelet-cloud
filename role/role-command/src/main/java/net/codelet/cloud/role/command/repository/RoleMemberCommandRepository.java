package net.codelet.cloud.role.command.repository;

import net.codelet.cloud.repository.BaseRepository;
import net.codelet.cloud.role.command.entity.RoleMemberCommandEntity;

import java.util.Optional;

/**
 * 角色组成员数据仓库。
 */
public interface RoleMemberCommandRepository extends BaseRepository<RoleMemberCommandEntity> {

    /**
     * 取得角色组成员信息。
     * @param roleId     角色组 ID
     * @param employeeId 职员 ID
     * @return 角色组成员关系信息
     */
    Optional<RoleMemberCommandEntity> findByRoleIdAndEmployeeId(String roleId, String employeeId);
}
