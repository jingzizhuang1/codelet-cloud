package net.codelet.cloud.organization.query.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.annotation.InternalAccessOnly;
import net.codelet.cloud.annotation.SetReferencedEntities;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.organization.query.api.OrganizationQueryApi;
import net.codelet.cloud.organization.query.dto.OrganizationHierarchyTreeDTO;
import net.codelet.cloud.organization.query.dto.OrganizationHierarchyQueryDTO;
import net.codelet.cloud.organization.query.entity.OrganizationQueryEntity;
import net.codelet.cloud.organization.query.service.OrganizationQueryService;
import net.codelet.cloud.organization.vo.OrganizationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@Api(tags = {"domain=组织", "biz=组织", "responsibility=查询"})
public class OrganizationQueryController extends BaseController implements OrganizationQueryApi {

    private final OrganizationQueryService organizationQueryService;

    @Autowired
    public OrganizationQueryController(
        OrganizationQueryService organizationQueryService
    ) {
        this.organizationQueryService = organizationQueryService;
    }

    @Override
    @InternalAccessOnly
    @ApiOperation("取得组织信息")
    public OrganizationQueryEntity get(@ApiParam("组织 ID") String orgId) {
        return organizationQueryService.get(orgId);
    }

    @Override
    @InternalAccessOnly
    @ApiOperation("检查组织是否存在")
    public Boolean exists(
        @ApiParam("组织 ID") String orgId,
        @ApiParam("组织类型") OrganizationType type
    ) {
        return organizationQueryService.exists(orgId, type);
    }

    @Override
    @CheckUserPrivilege
    @SetReferencedEntities
    @ApiOperation("取得组织的层级结构")
    public OrganizationHierarchyTreeDTO hierarchy(
        @ApiParam("组织 ID") String orgId,
        @Valid OrganizationHierarchyQueryDTO queryDTO
    ) {
        return organizationQueryService.hierarchy(orgId, queryDTO);
    }

    @Override
    @InternalAccessOnly
    @SetReferencedEntities
    @ApiOperation("批量取得组织信息")
    public List<OrganizationQueryEntity> batchGet(
        @ApiParam("组织 ID 集合") Set<String> entityIDs
    ) {
        return organizationQueryService.get(entityIDs);
    }
}
