package net.codelet.cloud.role.query.controller;

import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.annotation.SetReferencedEntities;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.role.query.api.RoleMemberQueryApi;
import net.codelet.cloud.role.query.dto.RoleMemberQueryDTO;
import net.codelet.cloud.role.query.entity.RoleMemberQueryEntity;
import net.codelet.cloud.role.query.service.RoleMemberQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = {"domain=角色与权限", "biz=角色组成员", "responsibility=查询"})
public class RoleMemberQueryController extends BaseController implements RoleMemberQueryApi {

    private final RoleMemberQueryService roleMemberQueryService;

    @Autowired
    public RoleMemberQueryController(
        RoleMemberQueryService roleMemberQueryService
    ) {
        this.roleMemberQueryService = roleMemberQueryService;
    }

    @Override
    @CheckUserPrivilege
    @SetReferencedEntities
    @ApiOperation("查询角色组成员")
    public Page<RoleMemberQueryEntity> members(
        @ApiParam("组织 ID") String orgId,
        @ApiParam("角色组 ID") String roleId,
        @Valid RoleMemberQueryDTO queryDTO
    ) {
        return roleMemberQueryService.members(orgId, roleId, queryDTO);
    }

    @Override
    @CheckUserPrivilege
    @SetReferencedEntities
    @ApiOperation("成员关系详细信息")
    public RoleMemberQueryEntity member(
        @ApiParam("组织 ID") String orgId,
        @ApiParam("角色组 ID") String roleId,
        @ApiParam("成员 ID、职员 ID 或用户 ID") String memberId
    ) {
        return roleMemberQueryService.member(memberId, orgId, roleId, null);
    }
}
