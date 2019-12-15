package net.codelet.cloud.role.command.controller;

import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.role.command.api.RoleMemberCommandApi;
import net.codelet.cloud.role.command.dto.RoleMemberJoinDTO;
import net.codelet.cloud.role.command.service.RoleMemberCommandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Set;

@RestController
@Api(tags = {"domain=角色与权限", "biz=角色组成员", "responsibility=命令"})
public class RoleMemberCommandController extends BaseController implements RoleMemberCommandApi {

    private final RoleMemberCommandService roleMemberCommandService;

    @Autowired
    public RoleMemberCommandController(
        RoleMemberCommandService roleMemberCommandService
    ) {
        this.roleMemberCommandService = roleMemberCommandService;
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("将职员加入到角色组")
    public void join(
        @ApiParam("组织 ID") String orgId,
        @ApiParam("角色组 ID") String roleId,
        @Valid RoleMemberJoinDTO membershipDTO
    ) {
        roleMemberCommandService
            .join(getOperator(), orgId, roleId, membershipDTO.getMemberIDs());
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("将职员从角色组退出")
    public void quit(
        @ApiParam("组织 ID") String orgId,
        @ApiParam("角色组 ID") String roleId,
        @ApiParam("用户 ID、职员 ID 或成员 ID 的集合") @Size(min = 1, max = 50) Set<String> memberIDs
    ) {
        roleMemberCommandService
            .quit(getOperator(), orgId, roleId, memberIDs);
    }
}
