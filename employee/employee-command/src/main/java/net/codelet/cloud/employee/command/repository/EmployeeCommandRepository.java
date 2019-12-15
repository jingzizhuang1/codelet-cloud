package net.codelet.cloud.employee.command.repository;

import net.codelet.cloud.employee.command.entity.EmployeeCommandEntity;
import net.codelet.cloud.repository.BaseRepository;

import java.util.List;
import java.util.Set;

/**
 * 职员数据仓库。
 */
public interface EmployeeCommandRepository extends BaseRepository<EmployeeCommandEntity> {

    /**
     * 批量取得职员信息。
     * @param companyId 公司 ID
     * @param userIDs   用户 ID 集合
     * @return 职员列表
     */
    List<EmployeeCommandEntity> findByCompanyIdAndUserIdInAndDeletedIsFalse(String companyId, Set<String> userIDs);
}
