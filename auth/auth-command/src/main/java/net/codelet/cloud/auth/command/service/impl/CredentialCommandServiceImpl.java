package net.codelet.cloud.auth.command.service.impl;

import net.codelet.cloud.auth.command.dto.CredentialsCreateDTO;
import net.codelet.cloud.auth.command.entity.CredentialCommandEntity;
import net.codelet.cloud.auth.command.entity.CredentialPasswordEntity;
import net.codelet.cloud.auth.command.entity.UserPasswordCommandEntity;
import net.codelet.cloud.auth.command.repository.CredentialCommandRepository;
import net.codelet.cloud.auth.command.repository.CredentialPasswordRepository;
import net.codelet.cloud.auth.command.repository.UserPasswordCommandRepository;
import net.codelet.cloud.auth.command.service.CredentialCommandService;
import net.codelet.cloud.auth.vo.CredentialType;
import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.error.AuthenticationError;
import net.codelet.cloud.error.BaseError;
import net.codelet.cloud.error.DuplicatedError;
import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.util.CryptoUtils;
import net.codelet.cloud.util.PasswordUtils;
import net.codelet.cloud.util.RegExpUtils;
import net.codelet.cloud.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户登录凭证命令服务。
 */
@Component
public class CredentialCommandServiceImpl implements CredentialCommandService {

    private final CredentialCommandRepository credentialCommandRepository;
    private final UserPasswordCommandRepository userPasswordCommandRepository;
    private final CredentialPasswordRepository credentialPasswordRepository;

    @Autowired
    public CredentialCommandServiceImpl(
        CredentialCommandRepository credentialCommandRepository,
        UserPasswordCommandRepository userPasswordCommandRepository,
        CredentialPasswordRepository credentialPasswordRepository
    ) {
        this.credentialCommandRepository = credentialCommandRepository;
        this.userPasswordCommandRepository = userPasswordCommandRepository;
        this.credentialPasswordRepository = credentialPasswordRepository;
    }

    /**
     * 创建登录凭证。
     * @param operator   操作者信息
     * @param userId     所有者用户 ID
     * @param type       凭证类型
     * @param credential 凭证
     * @return 登录凭证实体
     */
    @Override
    public CredentialCommandEntity createCredential(
        final OperatorDTO operator,
        final String userId,
        final CredentialType type,
        final String credential
    ) {
        CredentialCommandEntity credentialEntity = credentialCommandRepository
            .findByTypeAndCredentialAndDeletedIsFalse(type, credential)
            .orElse(null);

        if (credentialEntity != null) {
            if (credentialEntity.getUserId().equals(userId)) {
                return credentialEntity;
            }
            throw new DuplicatedError();
        }

        credentialEntity = new CredentialCommandEntity();
        credentialEntity.setUserId(userId);
        credentialEntity.setType(type);
        credentialEntity.setCredential(credential);
        credentialEntity.setCreatedAt(new Date());
        credentialEntity.setCreatedBy(operator == null ? userId : operator.getId());
        credentialEntity.updateRevision();
        return credentialCommandRepository.save(credentialEntity);
    }

    /**
     * 删除登录凭证。
     * @param operator   操作者信息
     * @param userId     所有者用户 ID
     * @param type       凭证类型
     * @param credential 凭证
     */
    @Override
    public void deleteCredential(
        final OperatorDTO operator,
        final String userId,
        final CredentialType type,
        final String credential
    ) {
        CredentialCommandEntity credentialEntity = credentialCommandRepository
            .findByUserIdAndTypeAndCredentialAndDeletedIsFalse(userId, type, credential)
            .orElse(null);

        if (credentialEntity == null) {
            throw new NotFoundError();
        }

        credentialEntity.setDeleted(true);
        credentialEntity.setDeletedAt(new Date());
        credentialEntity.setDeletedBy(operator == null ? userId : operator.getId());
        credentialEntity.updateRevision();
        credentialCommandRepository.save(credentialEntity);
    }

    /**
     * 设置登录用户名。
     * @param operator 操作者信息
     * @param userId   所有者用户 ID
     * @param username 登录用户名
     * @return 登录凭证实体
     */
    @Override
    public CredentialCommandEntity setUsername(
        final OperatorDTO operator,
        final String userId,
        final String username
    ) {
        List<CredentialCommandEntity> credentials = credentialCommandRepository
            .findByUserIdAndTypeAndDeletedIsFalse(userId, CredentialType.USERNAME);

        CredentialCommandEntity credentialEntity = credentials.size() == 0 ? null : credentials.get(0);
        Date timestamp = new Date();
        String operatorId = operator == null ? userId : operator.getId();

        if (credentialEntity != null && credentialEntity.getCredential().equals(username)) {
            return credentialEntity;
        }

        if (credentialEntity == null) {
            credentialEntity = new CredentialCommandEntity();
            credentialEntity.setUserId(userId);
            credentialEntity.setType(CredentialType.USERNAME);
            credentialEntity.setCreatedAt(timestamp);
            credentialEntity.setCreatedBy(operatorId);
        }

        credentialEntity.setCredential(username);
        credentialEntity.setLastModifiedAt(timestamp);
        credentialEntity.setLastModifiedBy(operatorId);
        credentialEntity.updateRevision();
        return credentialCommandRepository.save(credentialEntity);
    }


    /**
     * 重置登录密码。
     * @param operator   操作者信息
     * @param credential 用户 ID 或登录凭证
     * @param password   新密码
     */
    @Override
    @Transactional
    public void setPassword(
        final OperatorDTO operator,
        final String credential,
        final String password
    ) {
        String userId;
        if (!RegExpUtils.isID(credential)) {
            CredentialPasswordEntity credentialPasswordEntity
                = credentialPasswordRepository.findByCredential(credential).orElse(null);
            if (credentialPasswordEntity == null) {
                throw new NotFoundError();
            }
            userId = credentialPasswordEntity.getUserId();
        } else {
            userId = credential;
        }
        setPassword(operator, userId, null, password, false);
    }

    /**
     * 设置登录密码。
     * @param operator    操作者信息
     * @param userId      所有者用户 ID
     * @param oldPassword 原密码
     * @param newPassword 新密码
     */
    @Override
    @Transactional
    public void setPassword(
        final OperatorDTO operator,
        final String userId,
        final String oldPassword,
        final String newPassword
    ) {
        setPassword(operator, userId, oldPassword, newPassword, true);
    }

    /**
     * 设置登录密码。
     * @param operator           操作者信息
     * @param userId             所有者用户 ID
     * @param oldPassword        原密码
     * @param newPassword        新密码
     * @param requireOldPassword 是否必须指定旧密码
     */
    private void setPassword(
        final OperatorDTO operator,
        final String userId,
        final String oldPassword,
        final String newPassword,
        final boolean requireOldPassword
    ) {
        // 取得当前使用中的密码
        UserPasswordCommandEntity oldPasswordEntity = userPasswordCommandRepository
            .findByUserIdAndDeletedIsFalse(userId)
            .orElse(null);

        // 若旧密码不正确则返回错误
        if ((oldPassword == null
                && requireOldPassword
                && oldPasswordEntity != null)
            || (oldPassword != null
                && (oldPasswordEntity == null
                || !PasswordUtils.validatePassword(oldPassword, oldPasswordEntity.getPassword())))) {
            throw new AuthenticationError();
        }

        Date timestamp = new Date();
        String operatorId = operator == null ? userId : operator.getId();
        String passwordHash = CryptoUtils.md5(newPassword + "\r\n" + userId);

        // 若存在旧密码……
        if (oldPasswordEntity != null) {
            // 若新旧密码相同则结束
            if (oldPasswordEntity.getHash().equals(passwordHash)) {
                return;
            }
            // 将旧密码标记为已删除
            oldPasswordEntity.setDeleted(true);
            oldPasswordEntity.setDeletedAt(timestamp);
            oldPasswordEntity.setDeletedBy(operatorId);
            userPasswordCommandRepository.save(oldPasswordEntity);
        }

        // 保存新密码
        UserPasswordCommandEntity newPasswordEntity = new UserPasswordCommandEntity();
        newPasswordEntity.setUserId(userId);
        newPasswordEntity.setPassword(PasswordUtils.encryptPassword(newPassword, 12)); // TODO: read rounds from application configuration
        newPasswordEntity.setStrength(PasswordUtils.passwordStrength(newPassword));
        newPasswordEntity.setHash(passwordHash);
        newPasswordEntity.setCreatedAt(timestamp);
        newPasswordEntity.setCreatedBy(operatorId);
        userPasswordCommandRepository.save(newPasswordEntity);
    }

    /**
     * 创建认证凭证。
     * @param operator             操作者信息
     * @param userId               所有者用户 ID
     * @param credentialsCreateDTO 认证凭证批量创建表单数据
     */
    @Override
    @Transactional
    public void createCredentials(
        OperatorDTO operator,
        String userId,
        CredentialsCreateDTO credentialsCreateDTO
    ) {
        int created = 0;
        List<BaseError> errors = new ArrayList<>();

        // 创建登录凭证
        for (CredentialsCreateDTO.CredentialCreateDTO createDTO : credentialsCreateDTO.getCredentials()) {
            try {
                createCredential(operator, userId, createDTO.getType(), createDTO.getCredential());
                created++;
            } catch (DuplicatedError e) {
                errors.add(e);
            }
        }

        if (created == 0 && errors.size() > 0) {
            throw errors.get(0);
        }

        // 设置登录密码
        if (!StringUtils.isEmpty(credentialsCreateDTO.getPassword())) {
            setPassword(operator, userId, credentialsCreateDTO.getPassword());
        }
    }
}
