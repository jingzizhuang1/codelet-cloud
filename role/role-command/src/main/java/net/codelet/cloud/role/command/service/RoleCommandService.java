package net.codelet.cloud.role.command.service;

import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.role.command.dto.PrivilegeDTO;
import net.codelet.cloud.role.command.dto.RoleCreateDTO;
import net.codelet.cloud.role.command.dto.RoleUpdateDTO;
import net.codelet.cloud.role.command.entity.RoleCommandEntity;

import java.util.Collection;

/**
 * 角色组服务接口。
 */
public interface RoleCommandService {

    /**
     * 创建角色组。
     * @param operator  操作者
     * @param orgId     所属组织 ID
     * @param createDTO 角色组信息
     * @return 角色组信息
     */
    RoleCommandEntity create(OperatorDTO operator, String orgId, RoleCreateDTO createDTO);

    /**
     * 更新角色组信息。
     * @param operator  操作者
     * @param orgId     所属组织 ID
     * @param roleId    角色组 ID
     * @param updateDTO 角色组信息
     * @param revision  修订版本号
     */
    void update(OperatorDTO operator, String orgId, String roleId, RoleUpdateDTO updateDTO, Long revision);

    /**
     * 为角色分配权限。
     * @param operator      操作者
     * @param orgId         所属组织 ID
     * @param roleId        角色 ID
     * @param privilegeDTOs 权限列表
     */
    void assign(OperatorDTO operator, String orgId, String roleId, Collection<PrivilegeDTO> privilegeDTOs);

    /**
     * 删除角色组。
     * @param operator 操作者信息
     * @param orgId    所属组织
     * @param roleId   角色组 ID
     * @param revision 修订版本号
     */
    void delete(OperatorDTO operator, String orgId, String roleId, Long revision);
}
