package net.codelet.cloud.organization.command.api;

import net.codelet.cloud.organization.command.dto.CompanyCreateDTO;
import net.codelet.cloud.organization.command.dto.DivisionCreateDTO;
import net.codelet.cloud.organization.command.dto.GroupCreateDTO;
import net.codelet.cloud.organization.command.dto.OrganizationUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@FeignClient(
    name = "${services.organization.command.name:organization-command}",
    contextId = "organization-command"
)
public interface OrganizationCommandApi {

    /**
     * 创建公司。
     */
    @PostMapping("/companies")
    void createCompany(
        @RequestBody CompanyCreateDTO createDTO
    );

    /**
     * 创建事业部。
     * @param parentId 上级组织 ID
     */
    @PostMapping("/orgs/{parentId}/divisions")
    void createDivision(
        @PathVariable("parentId") String parentId,
        @RequestBody DivisionCreateDTO createDTO
    );

    /**
     * 创建工作组。
     * @param parentId 上级组织 ID
     */
    @PostMapping("/orgs/{parentId}/groups")
    void createGroup(
        @PathVariable("parentId") String parentId,
        @RequestBody GroupCreateDTO createDTO
    );

    /**
     * 将组织放置到指定的上级下。
     * @param parentId 上级组织
     * @param orgIDs   放置对象组织
     */
    @PutMapping("/orgs/{parentId}/orgs/{orgIDs}")
    void put(
        @PathVariable("parentId") String parentId,
        @PathVariable("orgIDs") Set<String> orgIDs
    );

    /**
     * 更新组织信息。
     * @param parentId  上级组织 ID
     * @param orgId     组织 ID
     * @param revision  修订版本号
     * @param updateDTO 组织信息
     */
    @PatchMapping("/orgs/{parentId}/orgs/{orgId}")
    void update(
        @PathVariable("parentId") String parentId,
        @PathVariable("orgId") String orgId,
        @RequestParam("revision") long revision,
        @RequestBody OrganizationUpdateDTO updateDTO
    );

    /**
     * 停用组织。
     * @param parentId 上级组织
     * @param orgId    删除对象组织
     * @param revision 修订版本号
     */
    @PostMapping("/orgs/{parentId}/orgs/{orgId}/disable")
    void disable(
        @PathVariable("parentId") String parentId,
        @PathVariable("orgId") String orgId,
        @RequestParam("revision") long revision
    );

    /**
     * 启用组织。
     * @param parentId 上级组织
     * @param orgId    删除对象组织
     * @param revision 修订版本号
     */
    @PostMapping("/orgs/{parentId}/orgs/{orgId}/enable")
    void enable(
        @PathVariable("parentId") String parentId,
        @PathVariable("orgId") String orgId,
        @RequestParam("revision") long revision
    );

    /**
     * 删除组织。
     * @param parentId 上级组织
     * @param childIDs 删除对象组织
     */
    @DeleteMapping("/orgs/{parentId}/orgs/{childIDs}")
    void delete(
        @PathVariable("parentId") String parentId,
        @PathVariable("childIDs") Set<String> childIDs
    );

    /**
     * 删除组织。
     * @param companyId 公司 ID
     * @param orgId     删除对象组织
     * @param revision  修订版本号
     */
    @DeleteMapping("/companies/{companyId}/orgs/{orgId}")
    void delete(
        @PathVariable("companyId") String companyId,
        @PathVariable("orgId") String orgId,
        @RequestParam("revision") long revision
    );
}
