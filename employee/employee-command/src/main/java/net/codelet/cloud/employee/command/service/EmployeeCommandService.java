package net.codelet.cloud.employee.command.service;

import net.codelet.cloud.dto.OperatorDTO;

import java.util.Collections;
import java.util.Set;

/**
 * 组织成员服务接口。
 */
public interface EmployeeCommandService {

    /**
     * 将用户加入到组织。
     * @param operator  操作者信息
     * @param companyId 公司 ID
     * @param userId    用户 ID
     */
    default void join(OperatorDTO operator, String companyId, String userId) {
        join(operator, companyId, Collections.singleton(userId));
    }

    /**
     * 将用户加入到组织。
     * @param operator  操作者信息
     * @param companyId 公司 ID
     * @param userIDs   用户 ID 集合
     */
    void join(OperatorDTO operator, String companyId, Set<String> userIDs);

    /**
     * 通过申请/接受邀请。
     * @param operator  操作者信息
     * @param companyId 公司 ID
     * @param userId    用户 ID
     */
    default void approve(OperatorDTO operator, String companyId, String userId) {
        approve(operator, companyId, Collections.singleton(userId));
    }

    /**
     * 通过申请/接受邀请。
     * @param operator  操作者信息
     * @param companyId 公司 ID
     * @param userIDs   用户 ID 集合
     */
    void approve(OperatorDTO operator, String companyId, Set<String> userIDs);

    /**
     * 驳回申请/拒绝邀请。
     * @param operator  操作者信息
     * @param companyId 公司 ID
     * @param userId    用户 ID
     * @param comment   批注
     */
    default void reject(OperatorDTO operator, String companyId, String userId, String comment) {
        reject(operator, companyId, Collections.singleton(userId), comment);
    }

    /**
     * 驳回申请/拒绝邀请。
     * @param operator  操作者信息
     * @param companyId 公司 ID
     * @param userIDs   用户 ID 集合
     * @param comment   批注
     */
    void reject(OperatorDTO operator, String companyId, Set<String> userIDs, String comment);

    /**
     * 将用户从组织退出。
     * @param operator  操作者信息
     * @param companyId 公司 ID
     * @param userId    用户 ID
     */
    default void quit(OperatorDTO operator, String companyId, String userId) {
        quit(operator, companyId, Collections.singleton(userId));
    }

    /**
     * 将用户从组织退出。
     * @param operator  操作者信息
     * @param companyId 公司 ID
     * @param userIDs   用户 ID 集合
     */
    void quit(OperatorDTO operator, String companyId, Set<String> userIDs);
}
