package net.codelet.cloud.role.command.service;

import net.codelet.cloud.dto.OperatorDTO;

import java.util.Set;

/**
 * 角色组成员服务接口。
 */
public interface RoleMemberCommandService {

    /**
     * 将职员加入到角色组。
     * @param operator  操作者
     * @param orgId     组织 ID
     * @param roleId    目标角色组 ID
     * @param memberIDs 成员职员 ID 或用户 ID 的集合
     */
    void join(OperatorDTO operator, String orgId, String roleId, Set<String> memberIDs);

    /**
     * 将成员从角色组退出。
     * @param operator  操作者
     * @param orgId     组织 ID
     * @param roleId    角色 ID
     * @param memberIDs 成员 ID 集合
     */
    void quit(OperatorDTO operator, String orgId, String roleId, Set<String> memberIDs);
}
