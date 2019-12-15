package net.codelet.cloud.organization.query.api;

import net.codelet.cloud.api.BaseQueryApi;
import net.codelet.cloud.organization.query.dto.OrganizationHierarchyQueryDTO;
import net.codelet.cloud.organization.query.dto.OrganizationHierarchyTreeDTO;
import net.codelet.cloud.organization.query.entity.OrganizationQueryEntity;
import net.codelet.cloud.organization.vo.OrganizationType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(
    name = "${services.organization.query.name:organization-query}",
    contextId = "organization-query"
)
public interface OrganizationQueryApi extends BaseQueryApi<OrganizationQueryEntity> {

    /**
     * 取得组织信息。
     * @param orgId 组织 ID
     * @return 组织信息
     */
    @GetMapping("/orgs/{orgId}")
    OrganizationQueryEntity get(@PathVariable("orgId") String orgId);

    /**
     * 检查组织是否存在。
     * @param orgId 组织 ID
     * @param type  组织类型
     * @return 组织是否存在
     */
    @GetMapping("/orgs/{orgId}/exists")
    Boolean exists(
        @PathVariable("orgId") String orgId,
        @RequestParam(value = "type", required = false) OrganizationType type
    );

    /**
     * 取得组织的层级结构。
     * @param orgId    组织 ID
     * @param queryDTO 查询过滤条件
     * @return 组织层级结构
     */
    @GetMapping("/orgs/{orgId}/hierarchy")
    OrganizationHierarchyTreeDTO hierarchy(
        @PathVariable("orgId") String orgId,
        OrganizationHierarchyQueryDTO queryDTO
    );

    /**
     * 批量取得组织信息。
     * @param entityIDs 实体 ID 集合。
     * @return 组织实体列表
     */
    @Override
    @PostMapping("/batch-get-organizations")
    List<OrganizationQueryEntity> batchGet(@RequestParam("entityIDs") Set<String> entityIDs);
}
