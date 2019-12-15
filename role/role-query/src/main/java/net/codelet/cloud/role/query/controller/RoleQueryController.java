package net.codelet.cloud.role.query.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.annotation.SetReferencedEntities;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.role.query.api.RoleQueryApi;
import net.codelet.cloud.role.query.dto.RoleQueryDTO;
import net.codelet.cloud.role.query.entity.RoleQueryEntity;
import net.codelet.cloud.role.query.entity.RoleWithPrivilegesQueryEntity;
import net.codelet.cloud.role.query.service.RoleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = {"domain=角色与权限", "biz=角色组", "responsibility=查询"})
public class RoleQueryController extends BaseController implements RoleQueryApi {

    private final RoleQueryService roleQueryService;

    @Autowired
    public RoleQueryController(
        RoleQueryService roleQueryService
    ) {
        this.roleQueryService = roleQueryService;
    }

    @Override
    @CheckUserPrivilege
    @SetReferencedEntities
    @ApiOperation("查询角色组")
    public Page<RoleQueryEntity> roles(
        @ApiParam("组织 ID") String orgId,
        @Valid RoleQueryDTO queryDTO
    ) {
        return roleQueryService.roles(orgId, queryDTO);
    }

    @Override
    @CheckUserPrivilege
    @SetReferencedEntities
    @ApiOperation("取得角色详细信息")
    public RoleWithPrivilegesQueryEntity role(
        @ApiParam("组织 ID") String orgId,
        @ApiParam("角色 ID") String roleId
    ) {
        return roleQueryService.role(roleId, orgId, null);
    }
}
