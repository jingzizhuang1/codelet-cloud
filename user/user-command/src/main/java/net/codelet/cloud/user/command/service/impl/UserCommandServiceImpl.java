package net.codelet.cloud.user.command.service.impl;

import net.codelet.cloud.auth.command.api.CredentialCommandApi;
import net.codelet.cloud.auth.command.dto.CredentialsCreateDTO;
import net.codelet.cloud.auth.command.dto.UserPasswordUpdateDTO;
import net.codelet.cloud.auth.command.dto.credential.UsernameUpdateDTO;
import net.codelet.cloud.auth.vo.CredentialType;
import net.codelet.cloud.constant.EntityIDs;
import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.error.ConflictError;
import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.user.command.dto.UserCreateDTO;
import net.codelet.cloud.user.command.dto.UserSignUpDTO;
import net.codelet.cloud.user.command.dto.UserUpdateDTO;
import net.codelet.cloud.user.command.entity.UserCommandEntity;
import net.codelet.cloud.user.command.repository.UserCommandRepository;
import net.codelet.cloud.user.command.service.UserCommandService;
import net.codelet.cloud.util.BeanUtils;
import net.codelet.cloud.util.PinyinUtils;
import net.codelet.cloud.util.StringUtils;
import net.codelet.cloud.vo.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 用户服务。
 */
@Component
public class UserCommandServiceImpl implements UserCommandService {

    private final UserCommandRepository userCommandRepository;
    private final CredentialCommandApi credentialCommandApi;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public UserCommandServiceImpl(
        UserCommandRepository userCommandRepository,
        CredentialCommandApi credentialCommandApi
    ) {
        this.userCommandRepository = userCommandRepository;
        this.credentialCommandApi = credentialCommandApi;
    }

    /**
     * 取得配置详细信息。
     * @param userId   用户 ID
     * @param revision 更新版本号
     * @return 用户实体
     */
    private UserCommandEntity get(String userId, Long revision) {
        UserCommandEntity user
            = userCommandRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new NotFoundError();
        }
        if (revision != null && !revision.equals(user.getRevision())) {
            throw new ConflictError();
        }
        return user;
    }

    /**
     * 创建系统用户。
     */
    @Override
    @Transactional
    public void createSystemUsers() {
        // 若尚未创建系统用户账号……
        if (!userCommandRepository.findById(EntityIDs.SYSTEM_USER_ID).isPresent()) {
            // 创建系统用户账号
            UserCreateDTO createDTO = new UserCreateDTO();
            createDTO.setName("System");
            UserCommandEntity systemUser = create(null, EntityIDs.SYSTEM_USER_ID, UserType.SYSTEM, createDTO);

            // 停用账号（系统用户账号不可用于登录）
            disable(null, EntityIDs.SYSTEM_USER_ID, systemUser.getRevision());
        }

        // 若尚未创建超级用户账号……
        if (!userCommandRepository.findById(EntityIDs.SUPER_USER_ID).isPresent()) {
            // 创建超级用户账号
            UserCreateDTO createDTO = new UserCreateDTO();
            createDTO.setName("Super");
            create(null, EntityIDs.SUPER_USER_ID, UserType.SUPER, createDTO);

            // 设置用户名登录凭证
            UsernameUpdateDTO usernameUpdateDTO = new UsernameUpdateDTO();
            usernameUpdateDTO.setUsername("super");
            credentialCommandApi.setUsername(EntityIDs.SUPER_USER_ID, usernameUpdateDTO);

            // 设置登录密码
            UserPasswordUpdateDTO passwordUpdateDTO = new UserPasswordUpdateDTO();
            passwordUpdateDTO.setNewPassword("super");
            credentialCommandApi.updatePassword(EntityIDs.SUPER_USER_ID, passwordUpdateDTO);
        }
    }

    /**
     * 创建用户账号。
     * @param operator  操作者信息
     * @param userType  用户类型
     * @param signUpDTO 用户创建表单
     * @return 用户实体信息
     */
    @Override
    @Transactional
    public UserCommandEntity create(
        final OperatorDTO operator,
        final UserType userType,
        final UserSignUpDTO signUpDTO
    ) {
        // 创建用户账号
        UserCreateDTO createDTO = new UserCreateDTO();
        BeanUtils.copyProperties(signUpDTO, createDTO);
        UserCommandEntity userEntity = create(operator, null, userType, createDTO);

        // 创建认证凭证
        CredentialsCreateDTO credentialsCreateDTO = new CredentialsCreateDTO();
        if (!StringUtils.isEmpty(signUpDTO.getUsername())) {
            credentialsCreateDTO.addCredential(CredentialType.USERNAME, signUpDTO.getUsername());
        }
        if (!StringUtils.isEmpty(signUpDTO.getEmail())) {
            credentialsCreateDTO.addCredential(CredentialType.EMAIL, signUpDTO.getEmail());
        }
        if (!StringUtils.isEmpty(signUpDTO.getMobile())) {
            credentialsCreateDTO.addCredential(CredentialType.MOBILE, signUpDTO.getMobile());
        }
        credentialsCreateDTO.setPassword(signUpDTO.getPassword());
        credentialCommandApi.createCredentials(userEntity.getId(), credentialsCreateDTO);

        return userEntity;
    }

    /**
     * 创建用户账号。
     * @param operator  操作者信息
     * @param userType  用户类型
     * @param createDTO 用户创建表单
     * @return 用户实体信息
     */
    @Override
    public UserCommandEntity create(
        final OperatorDTO operator,
        final UserType userType,
        final UserCreateDTO createDTO
    ) {
        return create(operator, null, userType, createDTO);
    }

    /**
     * 创建用户账号。
     * @param operator  操作者信息
     * @param userId    用户 ID
     * @param userType  用户类型
     * @param createDTO 用户创建表单
     * @return 用户实体信息
     */
    private UserCommandEntity create(
        final OperatorDTO operator,
        final String userId,
        final UserType userType,
        final UserCreateDTO createDTO
    ) {
        UserCommandEntity user = new UserCommandEntity();
        BeanUtils.copyProperties(createDTO, user);
        if (userId != null) {
            user.setId(userId);
        }
        user.setType(userType == null ? UserType.USER : userType);
        user.setNamePinyin(PinyinUtils.convert(user.getName()));
        user.setCreatedAt(new Date());
        user.setCreatedBy(operator == null ? user.getId() : operator.getId());
        user.updateRevision();
        return userCommandRepository.save(user);
    }

    /**
     * 更新用户账号。
     * @param operator  操作者信息
     * @param userId    用户 ID
     * @param revision  更新版本号
     * @param updateDTO 用户更新表单
     */
    @Override
    public void update(
        final OperatorDTO operator,
        final String userId,
        final Long revision,
        final UserUpdateDTO updateDTO
    ) {
        UserCommandEntity user = get(userId, revision);
        if (!BeanUtils.merge(updateDTO, user)) {
            return;
        }
        user.setNamePinyin(PinyinUtils.convert(user.getName()));
        user.setLastModifiedAt(new Date());
        user.setLastModifiedBy(operator.getId());
        user.updateRevision();
        userCommandRepository.save(user);
    }

    /**
     * 停用账号。
     * @param operator 操作者信息
     * @param userId   用户 ID
     * @param revision 更新版本号
     */
    @Override
    public void disable(
        final OperatorDTO operator,
        final String userId,
        final Long revision
    ) {
        UserCommandEntity user = get(userId, revision);
        if (user.getDisabled()) {
            return;
        }
        Date timestamp = new Date();
        String operatorId = operator == null ? EntityIDs.SYSTEM_USER_ID : operator.getId();
        user.setDisabled(true);
        user.setDisabledAt(timestamp);
        user.setDisabledBy(operatorId);
        user.setLastModifiedAt(timestamp);
        user.setLastModifiedBy(operatorId);
        user.updateRevision();
        userCommandRepository.save(user);
    }

    /**
     * 启用账号。
     * @param operator 操作者信息
     * @param userId   用户 ID
     * @param revision 更新版本号
     */
    @Override
    public void enable(
        final OperatorDTO operator,
        final String userId,
        final Long revision
    ) {
        UserCommandEntity user = get(userId, revision);
        if (!user.getDisabled()) {
            return;
        }
        Date timestamp = new Date();
        user.setDisabled(false);
        user.setLastModifiedAt(timestamp);
        user.setLastModifiedBy(operator.getId());
        user.updateRevision();
        userCommandRepository.save(user);
    }

    /**
     * 注销账号。
     * @param operator 操作者信息
     * @param userId   用户 ID
     * @param revision 更新版本号
     */
    @Override
    public void delete(
        final OperatorDTO operator,
        final String userId,
        final Long revision
    ) {
        UserCommandEntity user = get(userId, revision);
        if (user.getDeleted()) {
            return;
        }
        user.setDeleted(true);
        user.setDeletedAt(new Date());
        user.setDeletedBy(operator.getId());
        user.updateRevision();
        userCommandRepository.save(user);
    }
}
