package net.codelet.cloud.auth.command.service;

import net.codelet.cloud.auth.command.dto.CredentialsCreateDTO;
import net.codelet.cloud.auth.command.entity.CredentialCommandEntity;
import net.codelet.cloud.auth.vo.CredentialType;
import net.codelet.cloud.dto.OperatorDTO;

/**
 * 用户登录凭证命令服务接口。
 */
public interface CredentialCommandService {

    /**
     * 创建登录凭证。
     * @param operator   操作者信息
     * @param userId     所有者用户 ID
     * @param type       凭证类型
     * @param credential 凭证
     * @return 登录凭证实体
     */
    CredentialCommandEntity createCredential(
        OperatorDTO operator,
        String userId,
        CredentialType type,
        String credential
    );

    /**
     * 删除登录凭证。
     * @param operator   操作者信息
     * @param userId     所有者用户 ID
     * @param type       凭证类型
     * @param credential 凭证
     */
    void deleteCredential(
        OperatorDTO operator,
        String userId,
        CredentialType type,
        String credential
    );

    /**
     * 设置登录用户名。
     * @param operator 操作者信息
     * @param userId   所有者用户 ID
     * @param username 登录用户名
     * @return 登录凭证实体
     */
    CredentialCommandEntity setUsername(
        OperatorDTO operator,
        String userId,
        String username
    );

    /**
     * 重置登录密码。
     * @param operator   操作者信息
     * @param credential 用户 ID 或登录凭证
     * @param password   新密码
     */
    void setPassword(
        OperatorDTO operator,
        String credential,
        String password
    );

    /**
     * 设置登录密码。
     * @param operator    操作者信息
     * @param userId      所有者用户 ID
     * @param oldPassword 原密码
     * @param newPassword 新密码
     */
    void setPassword(
        OperatorDTO operator,
        String userId,
        String oldPassword,
        String newPassword
    );

    /**
     * 创建认证凭证。
     * @param operator  操作者信息
     * @param userId    所有者用户 ID
     * @param createDTO 认证凭证批量创建表单数据
     */
    void createCredentials(
        OperatorDTO operator,
        String userId,
        CredentialsCreateDTO createDTO
    );
}
