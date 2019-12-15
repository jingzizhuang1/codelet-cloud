package net.codelet.cloud.role.command.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.role.command.api.RoleCommandApi;
import net.codelet.cloud.role.command.dto.RoleCreateDTO;
import net.codelet.cloud.role.command.dto.RolePrivilegePutDTO;
import net.codelet.cloud.role.command.dto.RoleUpdateDTO;
import net.codelet.cloud.role.command.entity.RoleCommandEntity;
import net.codelet.cloud.role.command.service.RoleCommandService;
import net.codelet.cloud.role.command.service.RoleMemberCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Set;

@RestController
@Api(tags = {"domain=角色与权限", "biz=角色组", "responsibility=命令"})
public class RoleCommandController extends BaseController implements RoleCommandApi {

    private final RoleCommandService roleCommandService;
    private final RoleMemberCommandService roleMemberCommandService;

    @Autowired
    public RoleCommandController(
        RoleCommandService roleCommandService,
        RoleMemberCommandService roleMemberCommandService
    ) {
        this.roleCommandService = roleCommandService;
        this.roleMemberCommandService = roleMemberCommandService;
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("创建角色组")
    public void create(
        @ApiParam("所属组织 ID") String orgId,
        @Valid RoleCreateDTO createDTO
    ) {
        OperatorDTO operator = getOperator();

        RoleCommandEntity roleEntity = roleCommandService.create(getOperator(), orgId, createDTO);

        Set<String> memberIDs = createDTO.getMemberIDs();
        if (memberIDs == null || memberIDs.size() == 0) {
            memberIDs = Collections.singleton(operator.getId());
        }

        roleMemberCommandService.join(operator, orgId, roleEntity.getId(), memberIDs);
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("更新角色组信息")
    public void update(
        @ApiParam("组织 ID") String orgId,
        @ApiParam("角色组 ID") String roleId,
        @ApiParam("修订版本号") long revision,
        @Valid RoleUpdateDTO updateDTO
    ) {
        roleCommandService
            .update(getOperator(), orgId, roleId, updateDTO, revision);
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("为角色组分配权限")
    public void assign(
        @ApiParam("组织 ID") String orgId,
        @ApiParam("角色 ID") String roleId,
        @Valid RolePrivilegePutDTO privilegePutDTO
    ) {
        roleCommandService
            .assign(getOperator(), orgId, roleId, privilegePutDTO.getPrivileges());
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("删除角色组")
    public void delete(
        @ApiParam("组织 ID") String orgId,
        @ApiParam("角色组 ID") String roleId,
        @ApiParam("修订版本号") long revision
    ) {
        roleCommandService
            .delete(getOperator(), orgId, roleId, revision);
    }
}
