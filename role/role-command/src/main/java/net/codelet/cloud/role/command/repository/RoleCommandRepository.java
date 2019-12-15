package net.codelet.cloud.role.command.repository;

import net.codelet.cloud.repository.BaseRepository;
import net.codelet.cloud.role.command.entity.RoleCommandEntity;

import java.util.Optional;

/**
 * 角色组数据仓库。
 */
public interface RoleCommandRepository extends BaseRepository<RoleCommandEntity> {

    /**
     * 检查是否存在名称相同的角色组。
     * @param orgId 所属组织 ID
     * @param name  角色组名称
     * @return 检查结果
     */
    boolean existsByOrgIdAndNameAndDeletedIsFalse(String orgId, String name);

    /**
     * 取得角色组信息。
     * @param orgId  组织 ID
     * @param roleId 角色组 ID
     * @return 角色信息
     */
    Optional<RoleCommandEntity> findByOrgIdAndIdAndDeletedIsFalse(String orgId, String roleId);
}
