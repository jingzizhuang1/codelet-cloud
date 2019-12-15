package net.codelet.cloud.organization.command.service;

import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.organization.command.dto.OrganizationCreateDTO;
import net.codelet.cloud.organization.command.dto.OrganizationUpdateDTO;
import net.codelet.cloud.organization.command.entity.OrganizationCommandEntity;
import net.codelet.cloud.organization.vo.OrganizationType;

import java.util.Set;

/**
 * 组织服务接口。
 */
public interface OrganizationCommandService {

    /**
     * 取得组织信息。
     * @param id 组织 ID
     * @return 组织信息
     */
    OrganizationCommandEntity get(String id);

    /**
     * 创建组织。
     * @param operator  操作者
     * @param parentId  上级组织 ID
     * @param type      组织类型
     * @param createDTO 组织信息
     * @param <T>       组织信息范型
     * @return 组织信息
     */
    <T extends OrganizationCreateDTO> OrganizationCommandEntity create(
        OperatorDTO operator,
        String parentId,
        OrganizationType type,
        T createDTO
    );

    /**
     * 将组织放置到指定的上级下。
     * @param operator 操作者
     * @param parentId 目标上级组织 ID
     * @param orgIDs   操作对象组织 ID 的集合
     */
    void put(
        OperatorDTO operator,
        String parentId,
        Set<String> orgIDs
    );

    /**
     * 更新组织信息。
     * @param operator  操作者
     * @param parentId  上级组织 ID
     * @param orgId     组织 ID
     * @param revision  修订版本号
     * @param updateDTO 组织信息
     */
    void update(
        OperatorDTO operator,
        String parentId,
        String orgId,
        Long revision,
        OrganizationUpdateDTO updateDTO
    );

    /**
     * 停用组织。
     * @param operator 操作者
     * @param parentId 上级组织 ID
     * @param orgId    组织 ID
     * @param revision 修订版本号
     */
    void disable(
        OperatorDTO operator,
        String parentId,
        String orgId,
        Long revision
    );

    /**
     * 启用组织。
     * @param operator 操作者
     * @param parentId 上级组织 ID
     * @param orgId    组织 ID
     * @param revision 修订版本号
     */
    void enable(
        OperatorDTO operator,
        String parentId,
        String orgId,
        Long revision
    );

    /**
     * 删除与下级组织的层级关系。
     * @param operator 操作者
     * @param parentId 上级组织 ID
     * @param childIDs 组织 ID 集合
     */
    void deleteChildren(
        OperatorDTO operator,
        String parentId,
        Set<String> childIDs
    );

    /**
     * 删除组织。
     * @param operator  操作者
     * @param companyId 公司 ID
     * @param orgId     组织 ID
     * @param revision  修订版本号
     */
    void delete(
        OperatorDTO operator,
        String companyId,
        String orgId,
        Long revision
    );

    /**
     * 删除无上级组织的组织及其组织层级关系。
     * @param operator  操作者信息
     * @param companyId 公司 ID
     */
    void deleteOrganizationsWithNoParent(
        OperatorDTO operator,
        String companyId
    );
}
