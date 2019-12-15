package net.codelet.cloud.employee.query.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.annotation.InternalAccessOnly;
import net.codelet.cloud.annotation.SetReferencedEntities;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.employee.query.api.EmployeeQueryApi;
import net.codelet.cloud.employee.query.dto.EmployeeCompanyQueryDTO;
import net.codelet.cloud.employee.query.entity.EmployeeQueryEntity;
import net.codelet.cloud.employee.query.service.EmployeeQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@Api(tags = {"domain=职员", "biz=职员", "responsibility=查询"})
public class EmployeeQueryController extends BaseController implements EmployeeQueryApi {

    private final EmployeeQueryService employeeQueryService;

    @Autowired
    public EmployeeQueryController(EmployeeQueryService employeeQueryService) {
        this.employeeQueryService = employeeQueryService;
    }

    @Override
    @CheckUserPrivilege
    @SetReferencedEntities
    @ApiOperation("取得用户加入的公司")
    public Page<EmployeeQueryEntity> companies(
        @ApiParam("职员用户 ID") String userId,
        @Valid EmployeeCompanyQueryDTO queryDTO
    ) {
        queryDTO.setUserId(userId);
        return employeeQueryService.employees(queryDTO);
    }

    @Override
    @CheckUserPrivilege
    @SetReferencedEntities
    @ApiOperation("取得公司的职员")
    public Page<EmployeeQueryEntity> employees(
        @ApiParam("公司 ID") String companyId,
        @Valid EmployeeCompanyQueryDTO queryDTO
    ) {
        queryDTO.setCompanyId(companyId);
        return employeeQueryService.employees(queryDTO);
    }

    @Override
    @InternalAccessOnly
    @ApiOperation("查询公司的职员")
    public List<EmployeeQueryEntity> batchGet(
        @ApiParam("公司 ID") String companyId,
        @ApiParam("职员 ID 或用户 ID 集合") Set<String> entityIDs,
        @ApiParam("是否已通过申请/接受邀请") Boolean approved
    ) {
        return employeeQueryService.employees(companyId, entityIDs, approved);
    }

    @Override
    @InternalAccessOnly
    @ApiOperation("批量取得职员信息")
    public List<EmployeeQueryEntity> batchGet(@ApiParam("职员 ID 集合") Set<String> entityIDs) {
        return batchGet(null, entityIDs, null);
    }

    @Override
    @CheckUserPrivilege
    @SetReferencedEntities
    @ApiOperation("取得职员详细信息")
    public EmployeeQueryEntity employee(
        @ApiParam("公司 ID") String companyId,
        @ApiParam("职员 ID") String employeeId
    ) {
        return employeeQueryService.employee(companyId, employeeId);
    }
}
