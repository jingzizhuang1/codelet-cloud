package net.codelet.cloud.role.command.api;

import net.codelet.cloud.role.command.dto.RoleCreateDTO;
import net.codelet.cloud.role.command.dto.RolePrivilegePutDTO;
import net.codelet.cloud.role.command.dto.RoleUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "${services.role.command.name:role-command}",
    contextId = "role-command"
)
public interface RoleCommandApi {

    /**
     * 创建角色组。
     * @param orgId     组织 ID
     * @param createDTO 角色组信息
     */
    @PostMapping("/orgs/{orgId}/roles")
    void create(
        @PathVariable("orgId") String orgId,
        @RequestBody RoleCreateDTO createDTO
    );

    /**
     * 更新角色组信息。
     * @param orgId     组织 ID
     * @param roleId    角色组 ID
     * @param revision  修订版本号
     * @param updateDTO 角色组信息
     */
    @PatchMapping("/orgs/{orgId}/roles/{roleId}")
    void update(
        @PathVariable("orgId") String orgId,
        @PathVariable("roleId") String roleId,
        @RequestParam("revision") long revision,
        @RequestBody RoleUpdateDTO updateDTO
    );

    /**
     * 分配权限。
     * @param orgId           组织 ID
     * @param roleId          角色 ID
     * @param privilegePutDTO 权限组列表
     */
    @PutMapping("/orgs/{orgId}/roles/{roleId}/privileges")
    void assign(
        @PathVariable("orgId") String orgId,
        @PathVariable("roleId") String roleId,
        @RequestBody RolePrivilegePutDTO privilegePutDTO
    );

    /**
     * 删除角色组。
     * @param orgId     组织 ID
     * @param roleId    角色组 ID
     * @param revision  修订版本号
     */
    @DeleteMapping("/orgs/{orgId}/roles/{roleId}")
    void delete(
        @PathVariable("orgId") String orgId,
        @PathVariable("roleId") String roleId,
        @RequestParam("revision") long revision
    );
}
