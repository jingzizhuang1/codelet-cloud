package net.codelet.cloud.employee.query.service;

import net.codelet.cloud.employee.query.dto.EmployeeCompanyQueryDTO;
import net.codelet.cloud.employee.query.entity.EmployeeQueryEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

/**
 * 职员服务接口。
 */
public interface EmployeeQueryService {

    /**
     * 查询公司职员关系数据。
     * @param queryDTO 查询条件
     * @return 公司列表
     */
    Page<EmployeeQueryEntity> employees(EmployeeCompanyQueryDTO queryDTO);

    /**
     * 批量取得职员信息。
     * @param companyId   公司 ID
     * @param employeeIDs 职员 ID
     * @param approved    是否已通过申请/接受邀请
     * @return 职员信息列表
     */
    List<EmployeeQueryEntity> employees(String companyId, Set<String> employeeIDs, Boolean approved);

    /**
     * 取得公司职员详细信息。
     * @param companyId  公司 ID
     * @param employeeId 职员 ID
     * @return 公司职员关系数据
     */
    EmployeeQueryEntity employee(String companyId, String employeeId);
}
