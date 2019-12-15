package net.codelet.cloud.verification.command.service.impl;

import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.error.BusinessError;
import net.codelet.cloud.error.ConflictError;
import net.codelet.cloud.error.DuplicatedError;
import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.notification.query.api.NotificationTemplateQueryApi;
import net.codelet.cloud.util.BeanUtils;
import net.codelet.cloud.util.StringUtils;
import net.codelet.cloud.verification.command.dto.VerificationConfigurationUpdateDTO;
import net.codelet.cloud.verification.command.entity.VerificationConfigurationCommandEntity;
import net.codelet.cloud.verification.command.repository.VerificationConfigurationCommandRepository;
import net.codelet.cloud.verification.command.service.VerificationConfigurationCommandService;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static net.codelet.cloud.constant.EntityIDs.SYSTEM_USER_ID;

/**
 * 验证码配置服务。
 */
@Component
public class VerificationConfigurationCommandServiceImpl implements VerificationConfigurationCommandService {

    private final VerificationConfigurationCommandRepository verificationConfigurationCommandRepository;

    private final NotificationTemplateQueryApi notificationTemplateQueryApi;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public VerificationConfigurationCommandServiceImpl(
        VerificationConfigurationCommandRepository verificationConfigurationCommandRepository,
        NotificationTemplateQueryApi notificationTemplateQueryApi
    ) {
        this.verificationConfigurationCommandRepository = verificationConfigurationCommandRepository;
        this.notificationTemplateQueryApi = notificationTemplateQueryApi;
    }

    /**
     * 检查是否存在验证码配置信息。
     * @param keyType 验证码类型
     * @param purpose 验证码用途
     * @param deleted 是否已删除
     * @return 验证码配置信息是否存在
     */
    @Override
    public boolean exists(
        VerificationType keyType,
        VerificationPurpose purpose,
        Boolean deleted
    ) {
        if (deleted == null) {
            return verificationConfigurationCommandRepository
                .existsByKeyTypeAndPurpose(keyType, purpose);
        }
        return verificationConfigurationCommandRepository
            .existsByKeyTypeAndPurposeAndDeletedIs(keyType, purpose, deleted);
    }

    /**
     * 取得配置详细信息。
     * @param keyType  验证类型
     * @param purpose  验证码用途
     * @param revision 配置更新版本号
     * @return 配置信息
     */
    private VerificationConfigurationCommandEntity get(
        VerificationType keyType,
        VerificationPurpose purpose,
        Long revision
    ) {
        VerificationConfigurationCommandEntity configuration = verificationConfigurationCommandRepository
            .findByKeyTypeAndPurposeAndDeletedIsFalse(keyType, purpose)
            .orElse(null);
        if (revision != null) {
            if (configuration == null) {
                throw new NotFoundError();
            }
            if (!revision.equals(configuration.getRevision())) {
                throw new ConflictError();
            }
        } else if (configuration != null) {
            throw new DuplicatedError();
        }
        return configuration;
    }

    /**
     * 设置验证码配置。
     * @param operator  操作者信息
     * @param keyType   验证类型
     * @param purpose   验证码用途
     * @param revision  更新版本号
     * @param updateDTO 验证码配置信息
     * @return 是否创建了新的配置
     */
    @Override
    public boolean set(
        OperatorDTO operator,
        VerificationType keyType,
        VerificationPurpose purpose,
        Long revision,
        VerificationConfigurationUpdateDTO updateDTO
    ) {
        final String operatorId = operator == null ? SYSTEM_USER_ID : operator.getId();

        // 尝试取得配置信息
        VerificationConfigurationCommandEntity configuration = get(keyType, purpose, revision);

        // 检查消息模版是否存在
        if (!StringUtils.isEmpty(updateDTO.getTemplateId())
            && !notificationTemplateQueryApi.exists(updateDTO.getTemplateId(), false)) {
            throw new BusinessError("error.verification-configuration.template-not-found"); // TODO: set message
        }

        Date timestamp = new Date();

        // 新建时
        if (configuration == null) {
            configuration = new VerificationConfigurationCommandEntity();
            BeanUtils.copyProperties(updateDTO, configuration);
            configuration.setKeyType(keyType);
            configuration.setPurpose(purpose);
            configuration.setCreatedAt(timestamp);
            configuration.setCreatedBy(operatorId);
            configuration.updateRevision();
            verificationConfigurationCommandRepository.save(configuration);
            return true;
        }

        // 更新时
        if (BeanUtils.merge(updateDTO, configuration)) {
            configuration.setLastModifiedAt(timestamp);
            configuration.setLastModifiedBy(operatorId);
            configuration.updateRevision();
            verificationConfigurationCommandRepository.save(configuration);
        }

        return false;
    }

    /**
     * 停用验证码配置。
     * @param operator 操作者信息
     * @param keyType  验证类型
     * @param purpose  验证码用途
     * @param revision 更新版本号
     */
    @Override
    public void disable(
        OperatorDTO operator,
        VerificationType keyType,
        VerificationPurpose purpose,
        Long revision
    ) {
        VerificationConfigurationCommandEntity configuration = get(keyType, purpose, revision);

        if (configuration.getDisabled()) {
            return;
        }

        Date timestamp = new Date();
        configuration.setDisabled(true);
        configuration.setDisabledAt(timestamp);
        configuration.setDisabledBy(operator.getId());
        configuration.setLastModifiedAt(timestamp);
        configuration.setLastModifiedBy(operator.getId());
        configuration.updateRevision();
        verificationConfigurationCommandRepository.save(configuration);
    }

    /**
     * 启用验证码配置。
     * @param operator 操作者信息
     * @param keyType  验证类型
     * @param purpose  验证码用途
     * @param revision 更新版本号
     */
    @Override
    public void enable(
        OperatorDTO operator,
        VerificationType keyType,
        VerificationPurpose purpose,
        Long revision
    ) {
        VerificationConfigurationCommandEntity configuration = get(keyType, purpose, revision);

        if (!configuration.getDisabled()) {
            return;
        }

        Date timestamp = new Date();
        configuration.setDisabled(false);
        configuration.setLastModifiedAt(timestamp);
        configuration.setLastModifiedBy(operator.getId());
        configuration.updateRevision();
        verificationConfigurationCommandRepository.save(configuration);
    }

    /**
     * 删除验证码配置。
     * @param operator 操作者信息
     * @param keyType  验证类型
     * @param purpose  验证码用途
     * @param revision 更新版本号
     */
    @Override
    public void delete(
        OperatorDTO operator,
        VerificationType keyType,
        VerificationPurpose purpose,
        Long revision
    ) {
        VerificationConfigurationCommandEntity configuration = get(keyType, purpose, revision);

        if (configuration.getDeleted()) {
            return;
        }

        configuration.setDeleted(true);
        configuration.setDeletedAt(new Date());
        configuration.setDeletedBy(operator.getId());
        configuration.updateRevision();
        verificationConfigurationCommandRepository.save(configuration);
    }
}
