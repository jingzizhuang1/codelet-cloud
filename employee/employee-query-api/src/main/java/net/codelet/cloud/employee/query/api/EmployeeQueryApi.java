package net.codelet.cloud.employee.query.api;

import net.codelet.cloud.api.BaseQueryApi;
import net.codelet.cloud.employee.query.dto.EmployeeCompanyQueryDTO;
import net.codelet.cloud.employee.query.entity.EmployeeQueryEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(
    name = "${services.organization.query.name:organization-query}",
    contextId = "organization-member-query"
)
public interface EmployeeQueryApi extends BaseQueryApi<EmployeeQueryEntity> {

    /**
     * 取得用户加入的公司。
     * @param userId   用户 ID
     * @param queryDTO 查询条件
     * @return 公司职员关系数据列表
     */
    @GetMapping("/users/{userId}/companies")
    Page<EmployeeQueryEntity> companies(
        @PathVariable("userId") String userId,
        EmployeeCompanyQueryDTO queryDTO
    );

    /**
     * 查询公司的职员。
     * @param companyId 公司 ID
     * @param queryDTO  查询条件
     * @return 公司职员关系数据列表
     */
    @GetMapping("/companies/{companyId}/employees")
    Page<EmployeeQueryEntity> employees(
        @PathVariable("companyId") String companyId,
        EmployeeCompanyQueryDTO queryDTO
    );

    /**
     * 查询公司的职员。
     * @param companyId  公司 ID
     * @param entityIDs 查询条件
     * @return 职员实体列表
     */
    @PostMapping("/companies/{companyId}/batch-get-employees")
    List<EmployeeQueryEntity> batchGet(
        @PathVariable("companyId") String companyId,
        @RequestParam("entityIDs") Set<String> entityIDs,
        @RequestParam(value = "approved", required = false) Boolean required
    );

    /**
     * 批量取得职员信息。
     * @param entityIDs 实体 ID 集合。
     * @return 职员实体列表
     */
    @Override
    @PostMapping("/batch-get-employees")
    List<EmployeeQueryEntity> batchGet(@RequestParam("entityIDs") Set<String> entityIDs);

    /**
     * 取得职员详细信息。
     * @param companyId  公司 ID
     * @param employeeId 职员 ID
     * @return 公司职员关系数据
     */
    @GetMapping("/companies/{companyId}/employees/{employeeId}")
    EmployeeQueryEntity employee(
        @PathVariable("companyId") String companyId,
        @PathVariable("employeeId") String employeeId
    );
}
