package net.codelet.cloud.employee.command.service.impl;

import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.employee.command.entity.EmployeeCommandEntity;
import net.codelet.cloud.employee.command.repository.EmployeeCommandRepository;
import net.codelet.cloud.employee.command.service.EmployeeCommandService;
import net.codelet.cloud.error.BusinessError;
import net.codelet.cloud.error.NoPrivilegeError;
import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.organization.query.api.OrganizationQueryApi;
import net.codelet.cloud.organization.vo.OrganizationType;
import net.codelet.cloud.user.query.api.UserQueryApi;
import net.codelet.cloud.user.query.entity.UserQueryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 组织成员服务。
 */
@Component
public class EmployeeCommandServiceImpl implements EmployeeCommandService {

    private final UserQueryApi userQueryApi;

    private final OrganizationQueryApi organizationQueryApi;

    private final EmployeeCommandRepository employeeRepository;

    @Autowired
    public EmployeeCommandServiceImpl(
        UserQueryApi userQueryApi,
        OrganizationQueryApi organizationQueryApi,
        EmployeeCommandRepository employeeRepository
    ) {
        this.userQueryApi = userQueryApi;
        this.organizationQueryApi = organizationQueryApi;
        this.employeeRepository = employeeRepository;
    }

    /**
     * 将用户加入到组织。
     * @param operator  操作者信息
     * @param companyId 公司 ID
     * @param userIDs   用户 ID 集合
     */
    @Override
    public void join(OperatorDTO operator, String companyId, Set<String> userIDs) {
        // 检查公司是否存在
        if (!organizationQueryApi.exists(companyId, OrganizationType.COMPANY)) {
            throw new NotFoundError("error.employee.no-such-company"); // TODO: set message
        }

        // 检查用户是否存在
        List<UserQueryEntity> users = userQueryApi.batchGet(userIDs);
        if (users.size() < userIDs.size()) {
            throw new BusinessError("error.employee.no-such-user"); // TODO: set message
        }

        // 取得已加入的职员信息
        Map<String, EmployeeCommandEntity> employeesJoined = new HashMap<>();
        employeeRepository
            .findByCompanyIdAndUserIdInAndDeletedIsFalse(companyId, userIDs)
            .forEach(employeeEntity -> employeesJoined.put(employeeEntity.getUserId(), employeeEntity));

        // 将用户加入到公司
        Date timestamp = new Date();
        String operatorId = operator.getId();
        List<EmployeeCommandEntity> employeeEntities = new ArrayList<>();
        users.forEach(user -> {
            EmployeeCommandEntity employeeEntity;
            // 若用户已加入则更新确认状态
            if ((employeeEntity = employeesJoined.get(user.getId())) != null) {
                if (employeeEntity.getApproved() != null && employeeEntity.getApproved()) {
                    return;
                }
                employeeEntity.setApproved(null);
            // 否则创建新的职员记录
            } else {
                employeeEntity = new EmployeeCommandEntity();
                employeeEntity.setCompanyId(companyId);
                employeeEntity.setUserId(user.getId());
                employeeEntity.setCreatedAt(timestamp);
                employeeEntity.setCreatedBy(operatorId);
                employeeEntity.updateRevision();
            }
            employeeEntities.add(employeeEntity);
        });

        if (employeeEntities.size() == 0) {
            return;
        }

        employeeRepository.saveAll(employeeEntities);
    }

    /**
     * 通过申请/接受邀请。
     * @param operator  操作者信息
     * @param companyId 公司 ID
     * @param userIDs   用户 ID 集合
     */
    @Override
    public void approve(OperatorDTO operator, String companyId, Set<String> userIDs) {
        // 检查职员信息是否存在
        List<EmployeeCommandEntity> employeeEntities = getEmployees(companyId, userIDs);

        // 更新职员信息
        Date timestamp = new Date();
        String operatorId = operator.getId();
        List<EmployeeCommandEntity> employeesNotYetApproved = new ArrayList<>();
        employeeEntities.forEach(employeeEntity -> {
            // 若已通过则略过
            if (employeeEntity.getApproved() != null && employeeEntity.getApproved()) {
                return;
            }
            // 不得代他人接受邀请
            if (employeeEntity.getCreatedBy().equals(operatorId) && !employeeEntity.getUserId().equals(operatorId)) {
                throw new NoPrivilegeError(); // TODO: set message
            }
            employeeEntity.setApproved(true);
            employeeEntity.setApprovedAt(timestamp);
            employeeEntity.setApprovedBy(operatorId);
            employeeEntity.updateRevision();
            employeesNotYetApproved.add(employeeEntity);
        });

        if (employeesNotYetApproved.size() == 0) {
            return;
        }

        // 保存职员信息
        employeeRepository.saveAll(employeesNotYetApproved);
    }

    /**
     * 驳回申请/拒绝邀请。
     * @param operator  操作者信息
     * @param companyId 公司 ID
     * @param userIDs   用户 ID 集合
     * @param comment   批注
     */
    @Override
    public void reject(OperatorDTO operator, String companyId, Set<String> userIDs, String comment) {
        // 检查职员信息是否存在
        List<EmployeeCommandEntity> employeeEntities = getEmployees(companyId, userIDs);

        // 更新职员信息
        Date timestamp = new Date();
        String operatorId = operator.getId();
        List<EmployeeCommandEntity> employeesNotYetRejected = new ArrayList<>();
        employeeEntities.forEach(employeeEntity -> {
            // 若已拒绝或通过则略过
            if (employeeEntity.getApproved() != null) {
                return;
            }
            employeeEntity.setApproved(false);
            employeeEntity.setRejectedAt(timestamp);
            employeeEntity.setRejectedBy(operatorId);
            employeeEntity.setComment(comment);
            employeeEntity.updateRevision();
            employeesNotYetRejected.add(employeeEntity);
        });

        if (employeesNotYetRejected.size() == 0) {
            return;
        }

        // 保存职员信息
        employeeRepository.saveAll(employeesNotYetRejected);
    }

    /**
     * 将用户从组织退出。
     * @param operator  操作者信息
     * @param companyId 公司 ID
     * @param userIDs   用户 ID 集合
     */
    @Override
    public void quit(OperatorDTO operator, String companyId, Set<String> userIDs) {
        // 检查职员信息是否存在
        List<EmployeeCommandEntity> employeeEntities = getEmployees(companyId, userIDs);
        if (employeeEntities.size() == 0) {
            return;
        }

        // 更新职员信息
        Date timestamp = new Date();
        String operatorId = operator.getId();
        employeeEntities.forEach(employeeEntity -> {
            employeeEntity.setDeleted(true);
            employeeEntity.setDeletedAt(timestamp);
            employeeEntity.setDeletedBy(operatorId);
            employeeEntity.updateRevision();
        });

        // 保存职员信息
        employeeRepository.saveAll(employeeEntities);

        // TODO: emit employee deleted event
    }

    /**
     * 取得职员信息。
     * @param companyId 公司 ID
     * @param userIDs   用户 ID 集合
     * @return 职员信息列表
     */
    private List<EmployeeCommandEntity> getEmployees(String companyId, Set<String> userIDs) {
        List<EmployeeCommandEntity> employeeEntities = employeeRepository
            .findByCompanyIdAndUserIdInAndDeletedIsFalse(companyId, userIDs);
        if (employeeEntities.size() < userIDs.size()) {
            throw new BusinessError("error.employee.no-such-employee"); // TODO: set message
        }
        return employeeEntities;
    }
}
