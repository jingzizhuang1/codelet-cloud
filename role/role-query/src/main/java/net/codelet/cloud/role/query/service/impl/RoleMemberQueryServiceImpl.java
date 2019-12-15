package net.codelet.cloud.role.query.service.impl;

import net.codelet.cloud.error.ConflictError;
import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.role.query.dto.RoleMemberQueryDTO;
import net.codelet.cloud.role.query.entity.RoleMemberQueryEntity;
import net.codelet.cloud.role.query.repository.RoleMemberQueryRepository;
import net.codelet.cloud.role.query.service.RoleMemberQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色组成员服务。
 */
@Component
public class RoleMemberQueryServiceImpl implements RoleMemberQueryService {

    private final RoleMemberQueryRepository roleMemberQueryRepository;

    @Autowired
    public RoleMemberQueryServiceImpl(RoleMemberQueryRepository roleMemberQueryRepository) {
        this.roleMemberQueryRepository = roleMemberQueryRepository;
    }

    /**
     * 查询成员列表。
     * @param orgId    组织 ID
     * @param roleId   角色 ID
     * @param queryDTO 查询条件
     * @return 成员列表
     */
    @Override
    public Page<RoleMemberQueryEntity> members(String orgId, String roleId, RoleMemberQueryDTO queryDTO) {
        queryDTO.setOrgId(orgId);
        queryDTO.setRoleId(roleId);
        return roleMemberQueryRepository.findAllByCriteria(queryDTO, queryDTO.pageable());
    }

    /**
     * 取得成员关系详细信息。
     * @param memberId 成员 ID
     * @param orgId    组织 ID
     * @param roleId   角色组 ID
     * @param revision 修订版本号
     * @return 成员关系详细信息
     */
    @Override
    public RoleMemberQueryEntity member(String memberId, String orgId, String roleId, Long revision) {
        RoleMemberQueryEntity memberEntity = roleMemberQueryRepository
            .findOne((Specification<RoleMemberQueryEntity>) (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("id"), memberId),
                    criteriaBuilder.equal(root.get("employeeId"), memberId),
                    criteriaBuilder.equal(root.get("userId"), memberId)
                ));
                if (orgId != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("orgId"), orgId)));
                }
                if (roleId != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("roleId"), roleId)));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            })
            .orElse(null);
        if (memberEntity == null) {
            throw new NotFoundError();
        }
        if (revision != null && !revision.equals(memberEntity.getRevision())) {
            throw new ConflictError();
        }
        return memberEntity;
    }
}
