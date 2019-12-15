package net.codelet.cloud.role.query.api;

import net.codelet.cloud.role.query.dto.RoleMemberQueryDTO;
import net.codelet.cloud.role.query.entity.RoleMemberQueryEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "${services.role.query.name:role-query}",
    contextId = "role-member-query"
)
public interface RoleMemberQueryApi {

    /**
     * 查询角色组成员。
     * @param orgId  组织 ID
     * @param roleId 角色组 ID
     * @param queryDTO 查询条件
     * @return 成员列表
     */
    @GetMapping("/orgs/{orgId}/roles/{roleId}/members")
    Page<RoleMemberQueryEntity> members(
        @PathVariable("orgId") String orgId,
        @PathVariable("roleId") String roleId,
        RoleMemberQueryDTO queryDTO
    );

    /**
     * 取得成员关系详细信息。
     * @param orgId    组织 ID
     * @param roleId   角色组 ID
     * @param memberId 成员 ID、职员 ID 或用户 ID
     * @return 成员关系详细信息
     */
    @GetMapping("/orgs/{orgId}/roles/{roleId}/members/{memberId}")
    RoleMemberQueryEntity member(
        @PathVariable("orgId") String orgId,
        @PathVariable("roleId") String roleId,
        @PathVariable("memberId") String memberId
    );
}
