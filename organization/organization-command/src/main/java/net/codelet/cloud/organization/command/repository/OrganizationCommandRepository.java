package net.codelet.cloud.organization.command.repository;

import net.codelet.cloud.organization.command.entity.OrganizationCommandEntity;
import net.codelet.cloud.repository.BaseRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 组织数据仓库。
 */
public interface OrganizationCommandRepository extends BaseRepository<OrganizationCommandEntity> {

    /**
     * 取得组织。
     * @param id 组织 ID
     * @return 组织信息
     */
    Optional<OrganizationCommandEntity> findByIdAndDeletedIsFalse(String id);

    /**
     * 取得组织。
     * @param companyId 公司 ID
     * @param orgId     组织 ID
     * @return 组织信息
     */
    Optional<OrganizationCommandEntity> findByCompanyIdAndIdAndDeletedIsFalse(String companyId, String orgId);

    /**
     * 取得所有组织信息。
     * @param companyId 公司 ID
     * @param orgIDs    组织 ID 集合
     * @return 组织列表
     */
    List<OrganizationCommandEntity> findByCompanyIdAndIdInAndDeletedIsFalse(String companyId, Set<String> orgIDs);

    /**
     * 删除无上级组织的组织及其组织层级关系。
     * @param operatorId 操作者 ID
     * @param companyId  公司 ID
     * @return 删除组织数据件数
     */
    @Transactional
    @Procedure("delete_organizations_with_no_parent")
    int deleteOrganizationsWithNoParent(
        @Param("operator_id") String operatorId,
        @Param("company_id") String companyId
    );
}
