package net.codelet.cloud.role.query.service;

import net.codelet.cloud.role.query.dto.RoleMemberQueryDTO;
import net.codelet.cloud.role.query.entity.RoleMemberQueryEntity;
import org.springframework.data.domain.Page;

/**
 * 角色组成员服务接口。
 */
public interface RoleMemberQueryService {

    /**
     * 查询成员列表。
     * @param orgId    组织 ID
     * @param roleId   角色 ID
     * @param queryDTO 查询条件
     * @return 成员列表
     */
    Page<RoleMemberQueryEntity> members(String orgId, String roleId, RoleMemberQueryDTO queryDTO);

    /**
     * 取得成员关系详细信息。
     * @param memberId 成员 ID
     * @param orgId    组织 ID
     * @param roleId   角色组 ID
     * @param revision 修订版本号
     * @return 成员关系详细信息
     */
    RoleMemberQueryEntity member(String memberId, String orgId, String roleId, Long revision);
}
