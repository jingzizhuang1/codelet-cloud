package net.codelet.cloud.role.command.service.impl;

import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.employee.query.api.EmployeeQueryApi;
import net.codelet.cloud.employee.query.entity.EmployeeQueryEntity;
import net.codelet.cloud.error.BusinessError;
import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.role.command.entity.RoleCommandEntity;
import net.codelet.cloud.role.command.entity.RoleMemberCommandEntity;
import net.codelet.cloud.role.command.repository.RoleCommandRepository;
import net.codelet.cloud.role.command.repository.RoleMemberCommandRepository;
import net.codelet.cloud.role.command.service.RoleMemberCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * 角色组成员服务。
 */
@Component
public class RoleMemberCommandServiceImpl implements RoleMemberCommandService {

    private final EmployeeQueryApi employeeQueryApi;
    private final RoleCommandRepository roleRepository;
    private final RoleMemberCommandRepository roleMemberRepository;

    @Autowired
    public RoleMemberCommandServiceImpl(
        EmployeeQueryApi employeeQueryApi,
        RoleCommandRepository roleRepository,
        RoleMemberCommandRepository roleMemberRepository
    ) {
        this.employeeQueryApi = employeeQueryApi;
        this.roleRepository = roleRepository;
        this.roleMemberRepository = roleMemberRepository;
    }

    /**
     * 将职员加入到角色组。
     * @param operator  操作者
     * @param orgId     组织 ID
     * @param roleId    目标角色组 ID
     * @param memberIDs 成员职员 ID 或用户 ID 的集合
     */
    @Override
    @Transactional
    public void join(OperatorDTO operator, String orgId, String roleId, Set<String> memberIDs) {
        RoleCommandEntity roleEntity = roleRepository
            .findByOrgIdAndIdAndDeletedIsFalse(orgId, roleId)
            .orElse(null);
        if (roleEntity == null) {
            throw new NotFoundError();
        }

        String companyId = roleEntity.getCompanyId();

        List<EmployeeQueryEntity> employeeEntities = employeeQueryApi.batchGet(companyId, memberIDs, true);

        employeeEntities.forEach(employeeEntity -> {
            memberIDs.remove(employeeEntity.getId());
            memberIDs.remove(employeeEntity.getUserId());
        });

        if (memberIDs.size() > 0) {
            throw new BusinessError("error.role.employee-not-found"); // TODO: set message
        }

        Date timestamp = new Date();
        String operatorId = operator.getId();
        List<RoleMemberCommandEntity> roleMemberEntities = new ArrayList<>();

        employeeEntities.forEach(employeeEntity -> {
            RoleMemberCommandEntity memberEntity = roleMemberRepository
                .findByRoleIdAndEmployeeId(roleId, employeeEntity.getId())
                .orElse(null);

            // 未加入时创建新的成员关系记录
            if (memberEntity == null) {
                memberEntity = new RoleMemberCommandEntity();
                memberEntity.setCompanyId(companyId);
                memberEntity.setOrgId(orgId);
                memberEntity.setRoleId(roleId);
                memberEntity.setEmployeeId(employeeEntity.getId());
                memberEntity.setUserId(employeeEntity.getUserId());
                memberEntity.setCreatedAt(timestamp);
                memberEntity.setCreatedBy(operatorId);
            // 若已加入但已删除则恢复加入关系
            } else if (memberEntity.getDeleted()) {
                memberEntity.setLastModifiedAt(timestamp);
                memberEntity.setLastModifiedBy(operatorId);
                memberEntity.setDeleted(false);
            // 否则不做处理
            } else {
                return;
            }

            memberEntity.updateRevision();
            roleMemberEntities.add(memberEntity);
        });

        if (roleMemberEntities.size() == 0) {
            return;
        }

        // 保存成员信息
        roleMemberRepository.saveAll(roleMemberEntities);
    }

    /**
     * 将成员从角色组退出。
     * @param operator  操作者
     * @param orgId     组织 ID
     * @param roleId    角色 ID
     * @param memberIDs 成员 ID 集合
     */
    @Override
    @Transactional
    public void quit(OperatorDTO operator, String orgId, String roleId, Set<String> memberIDs) {
        // 取得成员信息
        List<RoleMemberCommandEntity> roleMemberEntities = roleMemberRepository
            .findAll((Specification<RoleMemberCommandEntity>) (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                CriteriaBuilder.In<String> memberIdIn = criteriaBuilder.in(root.get("id"));
                CriteriaBuilder.In<String> employeeIdIn = criteriaBuilder.in(root.get("employeeId"));
                CriteriaBuilder.In<String> userIdIn = criteriaBuilder.in(root.get("userId"));
                for (String memberId : memberIDs) {
                    memberIdIn.value(memberId);
                    employeeIdIn.value(memberId);
                    userIdIn.value(memberId);
                }
                predicates.add(criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("orgId"), orgId),
                    criteriaBuilder.equal(root.get("roleId"), roleId),
                    criteriaBuilder.or(memberIdIn, employeeIdIn, userIdIn),
                    criteriaBuilder.equal(root.get("deleted"), false)
                ));
                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            });

        if (roleMemberEntities.size() == 0) {
            return;
        }

        Date timestamp = new Date();
        String operatorId = operator.getId();

        roleMemberEntities.forEach(memberEntity -> {
            memberEntity.setDeleted(true);
            memberEntity.setDeletedAt(timestamp);
            memberEntity.setDeletedBy(operatorId);
            memberEntity.updateRevision();
        });

        roleMemberRepository.saveAll(roleMemberEntities);
    }
}
