package net.codelet.cloud.role.query.api;

import net.codelet.cloud.role.query.dto.RoleQueryDTO;
import net.codelet.cloud.role.query.entity.RoleQueryEntity;
import net.codelet.cloud.role.query.entity.RoleWithPrivilegesQueryEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "${services.role.query.name:role-query}",
    contextId = "role-query"
)
public interface RoleQueryApi {

    /**
     * 查询角色组。
     * @param orgId    组织 ID
     * @param queryDTO 查询条件
     * @return 角色组列表
     */
    @GetMapping("/orgs/{orgId}/roles")
    Page<RoleQueryEntity> roles(
        @PathVariable("orgId") String orgId,
        RoleQueryDTO queryDTO
    );

    /**
     * 取得角色详细信息。
     * @param orgId  组织 ID
     * @param roleId 角色 ID
     * @return 角色信息
     */
    @GetMapping("/orgs/{orgId}/roles/{roleId}")
    RoleWithPrivilegesQueryEntity role(
        @PathVariable("orgId") String orgId,
        @PathVariable("roleId") String roleId
    );
}
