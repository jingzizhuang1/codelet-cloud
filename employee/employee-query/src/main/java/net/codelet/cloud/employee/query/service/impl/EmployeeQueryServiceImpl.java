package net.codelet.cloud.employee.query.service.impl;

import net.codelet.cloud.employee.query.dto.EmployeeCompanyQueryDTO;
import net.codelet.cloud.employee.query.entity.EmployeeQueryEntity;
import net.codelet.cloud.employee.query.repository.EmployeeQueryRepository;
import net.codelet.cloud.employee.query.service.EmployeeQueryService;
import net.codelet.cloud.error.NotFoundError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 职员服务。
 */
@Component
public class EmployeeQueryServiceImpl implements EmployeeQueryService {

    private final EmployeeQueryRepository employeeQueryRepository;

    @Autowired
    public EmployeeQueryServiceImpl(EmployeeQueryRepository employeeQueryRepository) {
        this.employeeQueryRepository = employeeQueryRepository;
    }

    /**
     * 查询公司职员关系数据。
     * @param queryDTO 查询条件
     * @return 公司列表
     */
    @Override
    public Page<EmployeeQueryEntity> employees(EmployeeCompanyQueryDTO queryDTO) {
        return employeeQueryRepository.findAllByCriteria(queryDTO, queryDTO.pageable());
    }

    /**
     * 批量取得职员信息。
     * @param companyId   公司 ID
     * @param employeeIDs 职员 ID 集合
     * @param approved    是否已通过申请/接受邀请
     * @return 职员信息列表
     */
    @Override
    public List<EmployeeQueryEntity> employees(String companyId, Set<String> employeeIDs, Boolean approved) {
        return employeeQueryRepository.findAll((Specification<EmployeeQueryEntity>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 指定公司 ID 时用职员 ID 参数匹配职员 ID 及用户 ID
            if (companyId != null) {
                CriteriaBuilder.In<String> employeeIdIn = criteriaBuilder.in(root.get("id"));
                CriteriaBuilder.In<String> userIdIn = criteriaBuilder.in(root.get("userId"));
                for (String employeeId : employeeIDs) {
                    employeeIdIn.value(employeeId);
                    userIdIn.value(employeeId);
                }
                predicates.add(criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("companyId"), companyId),
                    criteriaBuilder.or(employeeIdIn, userIdIn)
                ));
            // 否则职员 ID 参数仅与职员 ID 匹配
            } else {
                CriteriaBuilder.In<String> employeeIdIn = criteriaBuilder.in(root.get("id"));
                for (String employeeId : employeeIDs) {
                    employeeIdIn.value(employeeId);
                }
                predicates.add(criteriaBuilder.and(employeeIdIn));
            }

            if (approved != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("approved"), approved)));
            }

            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("deleted"), false)));

            return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
        });
    }

    /**
     * 取得公司职员详细信息。
     * @param companyId  公司 ID
     * @param employeeId 职员 ID
     * @return 公司职员关系数据
     */
    @Override
    public EmployeeQueryEntity employee(String companyId, String employeeId) {
        EmployeeCompanyQueryDTO queryDTO = new EmployeeCompanyQueryDTO();
        queryDTO.setId(employeeId);
        queryDTO.setCompanyId(companyId);
        EmployeeQueryEntity employeeEntity
            = employeeQueryRepository.findOneByCriteria(queryDTO).orElse(null);
        if (employeeEntity == null) {
            throw new NotFoundError();
        }
        return employeeEntity;
    }
}
