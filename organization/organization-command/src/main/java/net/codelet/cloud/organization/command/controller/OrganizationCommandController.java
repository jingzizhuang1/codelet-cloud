package net.codelet.cloud.organization.command.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.constant.PrivilegeScopes;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.employee.command.api.EmployeeCommandApi;
import net.codelet.cloud.organization.command.api.OrganizationCommandApi;
import net.codelet.cloud.organization.command.dto.*;
import net.codelet.cloud.organization.command.entity.OrganizationCommandEntity;
import net.codelet.cloud.organization.command.service.OrganizationCommandService;
import net.codelet.cloud.organization.vo.OrganizationType;
import net.codelet.cloud.role.command.api.RoleCommandApi;
import net.codelet.cloud.role.command.dto.PrivilegeDTO;
import net.codelet.cloud.role.command.dto.RoleCreateDTO;
import net.codelet.cloud.util.I18nUtils;
import net.codelet.cloud.vo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@Api(tags = {"domain=组织", "biz=组织", "responsibility=命令"})
public class OrganizationCommandController extends BaseController implements OrganizationCommandApi {

    private final OrganizationCommandService organizationCommandService;
    private final EmployeeCommandApi employeeCommandApi;
    private final RoleCommandApi roleCommandApi;

    @Autowired
    public OrganizationCommandController(
        OrganizationCommandService organizationCommandService,
        EmployeeCommandApi employeeCommandApi,
        RoleCommandApi roleCommandApi
    ) {
        this.organizationCommandService = organizationCommandService;
        this.employeeCommandApi = employeeCommandApi;
        this.roleCommandApi = roleCommandApi;
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("创建公司")
    public void createCompany(
        @Valid CompanyCreateDTO createDTO
    ) {
        create(getOperator(), null, OrganizationType.COMPANY, createDTO);
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation(
        value = "创建事业部",
        notes = "事业部类型即为事业部的组织结构类型（Organizational Structure Type），包括以下几种类型："
            + "<ul>"
            + "<li>职能型（Functional），以职能划分的事业部，如：财务部、市场部、营销部、行政部等</li>"
            + "<li>产品型（Product-Based），根据产品划分的事业部，如：大型车制造部、中型车制造部</li>"
            + "<li>项目（Project），以项目为单位划分的事业部</li>"
            + "<li>市场型（Market-Based），以目标客户划分的事业部</li>"
            + "<li>区域型（Geographical），以地理区域划分的事业部，如：北美事业部</li>"
            + "<li>流程型（Process-Based），以生产流程划分的事业部，如：研发部、制造部</li>"
            + "</ul>"
    )
    public void createDivision(
        @ApiParam("上级组织 ID") String parentId,
        @Valid DivisionCreateDTO createDTO
    ) {
        create(getOperator(), parentId, OrganizationType.DIVISION, createDTO);
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("创建工作组")
    public void createGroup(
        @ApiParam("上级组织 ID") String parentId,
        @Valid GroupCreateDTO createDTO
    ) {
        create(getOperator(), parentId, OrganizationType.GROUP, createDTO);
    }

    /**
     * 创建组织。
     * @param operator  操作者
     * @param parentId  上级组织 ID
     * @param type      组织的类型
     * @param createDTO 组织信息
     * @param <T>       组织信息范型
     */
    private <T extends OrganizationCreateDTO> void create(
        OperatorDTO operator,
        String parentId,
        OrganizationType type,
        T createDTO
    ) {
        final String operatorId = operator.getId();

        // 创建组织
        OrganizationCommandEntity organizationEntity = organizationCommandService
            .create(operator, parentId, type, createDTO);

        Set<String> administratorIDs = createDTO.getAdministratorIDs();
        if (administratorIDs == null) {
            administratorIDs = new HashSet<>();
        }
        if (administratorIDs.size() == 0) {
            administratorIDs.add(operatorId);
        }

        final String companyId = organizationEntity.getCompanyId();

        // 将指定的管理员用户加入到组织的公司
        employeeCommandApi.join(companyId, administratorIDs);
        if (administratorIDs.contains(operatorId)) {
            employeeCommandApi.accept(companyId);
        }

        final String orgId = organizationEntity.getId();

        PrivilegeDTO privilege = new PrivilegeDTO();
        privilege.setScope(PrivilegeScopes.ALL);
        privilege.setPermission(Permission.ALL);

        Set<PrivilegeDTO> privileges = new HashSet<>();
        privileges.add(privilege);

        RoleCreateDTO roleCreateDTO = new RoleCreateDTO();
        roleCreateDTO.setName(I18nUtils.message("role.administrator"));
        roleCreateDTO.setMemberIDs(administratorIDs);
        roleCreateDTO.setPrivileges(privileges);

        // 创建管理员角色组
        roleCommandApi.create(orgId, roleCreateDTO);
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("将组织放置到指定的上级下")
    public void put(
        @ApiParam("目标上级组织 ID") String parentId,
        @ApiParam("操作对象组织 ID 的集合") Set<String> orgIDs
    ) {
        // TODO: 检查当前用户拥有对 orgIDs 所有组织操作的权限
        organizationCommandService.put(getOperator(), parentId, orgIDs);
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("更新组织信息")
    public void update(
        @ApiParam("上级组织 ID") String parentId,
        @ApiParam("组织 ID") String orgId,
        @ApiParam("修订版本号") long revision,
        @Valid OrganizationUpdateDTO updateDTO
    ) {
        organizationCommandService
            .update(getOperator(), parentId, orgId, revision, updateDTO);
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("停用组织")
    public void disable(
        @ApiParam("上级组织 ID") String parentId,
        @ApiParam("删除对象组织 ID") String orgId,
        @ApiParam("修订版本号") long revision
    ) {
        organizationCommandService
            .disable(getOperator(), parentId, orgId, revision);
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("启用组织")
    public void enable(
        @ApiParam("上级组织 ID") String parentId,
        @ApiParam("删除对象组织 ID") String orgId,
        @ApiParam("修订版本号") long revision
    ) {
        organizationCommandService
            .enable(getOperator(), parentId, orgId, revision);
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation(
        value = "从指定的上级组织的下级组织中移除组织",
        notes = "该操作用于解除组织与其上级的层级关系，仅当解除层级关系后下级组织无任何其他上级时，下级组织才会被删除。"
    )
    public void delete(
        @ApiParam("上级组织 ID") String parentId,
        @ApiParam("删除对象组织 ID") Set<String> childIDs
    ) {
        OperatorDTO operator = getOperator();
        OrganizationCommandEntity parent = organizationCommandService.get(parentId);
        organizationCommandService.deleteChildren(operator, parentId, childIDs);
        organizationCommandService.deleteOrganizationsWithNoParent(operator, parent.getCompanyId());
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation(
        value = "删除组织及其所有组织关系",
        notes = "若删除组织后，其原下级组织无任何层级关系，则其下级组织也将被删除。"
    )
    public void delete(
        @ApiParam("所属公司 ID") String companyId,
        @ApiParam("删除对象组织 ID") String orgId,
        @ApiParam("修订版本号") long revision
    ) {
        OperatorDTO operator = getOperator();
        organizationCommandService.delete(operator, companyId, orgId, revision);
        organizationCommandService.deleteOrganizationsWithNoParent(operator, companyId);
    }
}
