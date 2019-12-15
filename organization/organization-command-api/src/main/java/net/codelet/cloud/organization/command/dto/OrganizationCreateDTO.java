package net.codelet.cloud.organization.command.dto;

import java.util.Set;

/**
 * 组织信息创建数据传输对象。
 */
public interface OrganizationCreateDTO {

    /**
     * 取得管理员用户 ID 的集合。
     * @return 管理员用户 ID 的集合
     */
    Set<String> getAdministratorIDs();
}
