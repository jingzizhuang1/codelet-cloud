package net.codelet.cloud.role.command.api;

import net.codelet.cloud.role.command.dto.RoleMemberJoinDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(
    name = "${services.role.command.name:role-command}",
    contextId = "role-member-command"
)
public interface RoleMemberCommandApi {

    /**
     * 将职员加入到角色组。
     * @param orgId         组织 ID
     * @param roleId        角色组 ID
     * @param membershipDTO 成员列表数据
     */
    @PostMapping("/orgs/{orgId}/roles/{roleId}/members")
    void join(
        @PathVariable("orgId") String orgId,
        @PathVariable("roleId") String roleId,
        @RequestBody RoleMemberJoinDTO membershipDTO
    );

    /**
     * 将职员从角色组退出。
     * @param orgId     组织 ID
     * @param roleId    角色组 ID
     * @param memberIDs 成员列表
     */
    @DeleteMapping("/orgs/{orgId}/roles/{roleId}/members/{memberIDs}")
    void quit(
        @PathVariable("orgId") String orgId,
        @PathVariable("roleId") String roleId,
        @PathVariable("memberIDs") Set<String> memberIDs
    );
}
