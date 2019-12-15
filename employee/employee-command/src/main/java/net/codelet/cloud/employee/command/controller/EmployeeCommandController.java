package net.codelet.cloud.employee.command.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.employee.command.api.EmployeeCommandApi;
import net.codelet.cloud.employee.command.dto.MembershipApplicationRejectDTO;
import net.codelet.cloud.employee.command.service.EmployeeCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Set;

@RestController
@Api(tags = {"domain=职员", "biz=职员", "responsibility=命令"})
public class EmployeeCommandController extends BaseController implements EmployeeCommandApi {

    private final EmployeeCommandService employeeCommandService;

    @Autowired
    public EmployeeCommandController(
        EmployeeCommandService employeeCommandService
    ) {
        this.employeeCommandService = employeeCommandService;
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("申请加入到公司")
    public void join(
        @ApiParam("公司 ID") String companyId
    ) {
        employeeCommandService
            .join(getOperator(), companyId, getOperator().getId());
    }

    @Override
    @CheckUserPrivilege(privileges = {"membership"})
    @ApiOperation("邀请用户加入到公司")
    public void join(
        @ApiParam("公司 ID") String companyId,
        @ApiParam("用户 ID 列表") @Size(max = 50) Set<String> userIDs
    ) {
        employeeCommandService
            .join(getOperator(), companyId, userIDs);
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("接受邀请")
    public void accept(
        @ApiParam("公司 ID") String companyId
    ) {
        employeeCommandService
            .approve(getOperator(), companyId, getOperator().getId());
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("通过申请")
    public void approve(
        @ApiParam("公司 ID") String companyId,
        @ApiParam("用户 ID 列表") Set<String> userIDs
    ) {
        employeeCommandService
            .approve(getOperator(), companyId, userIDs);
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("拒绝邀请")
    public void refuse(
        @ApiParam("公司 ID") String companyId
    ) {
        employeeCommandService
            .reject(getOperator(), companyId, getOperator().getId(), "");
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("驳回申请")
    public void reject(
        @ApiParam("公司 ID") String companyId,
        @ApiParam("用户 ID 列表") Set<String> userIDs,
        @Valid MembershipApplicationRejectDTO rejectDTO
    ) {
        employeeCommandService
            .reject(getOperator(), companyId, userIDs, rejectDTO.getComment());
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("从公司退出")
    public void quit(
        @ApiParam("公司 ID") String companyId
    ) {
        employeeCommandService
            .quit(getOperator(), companyId, getOperator().getId());
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("将用户从公司退出")
    public void quit(
        @ApiParam("公司 ID") String companyId,
        @ApiParam("用户 ID 列表") Set<String> userIDs
    ) {
        employeeCommandService
            .quit(getOperator(), companyId, userIDs);
    }
}
