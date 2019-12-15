package net.codelet.cloud.notification.command.service.impl;

import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.error.BusinessError;
import net.codelet.cloud.error.ConflictError;
import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.event.EventPublisher;
import net.codelet.cloud.notification.command.dto.SmsConfigurationCreateDTO;
import net.codelet.cloud.notification.command.dto.SmsConfigurationUpdateDTO;
import net.codelet.cloud.notification.command.entity.SmsConfigurationCommandEntity;
import net.codelet.cloud.notification.command.event.MailConfigurationUpdatedEvent;
import net.codelet.cloud.notification.command.repository.SmsConfigurationCommandRepository;
import net.codelet.cloud.notification.command.service.SmsConfigurationCommandService;
import net.codelet.cloud.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 短信配置服务。
 */
@Component
public class SmsConfigurationCommandServiceImpl implements SmsConfigurationCommandService {

    private final EventPublisher eventPublisher;
    private final SmsConfigurationCommandRepository configurationRepository;

    @Autowired
    public SmsConfigurationCommandServiceImpl(
        EventPublisher eventPublisher,
        SmsConfigurationCommandRepository configurationRepository
    ) {
        this.eventPublisher = eventPublisher;
        this.configurationRepository = configurationRepository;
    }

    /**
     * 创建短信配置。
     * @param operator  操作者信息
     * @param createDTO 短信配置信息
     */
    @Override
    public SmsConfigurationCommandEntity create(OperatorDTO operator, SmsConfigurationCreateDTO createDTO) {
        SmsConfigurationCommandEntity configurationEntity = new SmsConfigurationCommandEntity();
        BeanUtils.copyProperties(createDTO, configurationEntity);
        configurationEntity.setCreatedAt(new Date());
        configurationEntity.setCreatedBy(operator.getId());
        configurationEntity.updateRevision();
        configurationEntity.setIsDefault(configurationRepository.countByDeletedIsFalse() == 0);
        return configurationRepository.save(configurationEntity);
    }

    /**
     * 更新短信配置。
     * @param operator        操作者信息
     * @param configurationId 配置 ID
     * @param updateDTO       短信配置信息
     * @param revision        修订版本号
     */
    @Override
    public void update(
        OperatorDTO operator,
        String configurationId,
        SmsConfigurationUpdateDTO updateDTO,
        Long revision
    ) {
        SmsConfigurationCommandEntity configurationEntity = get(configurationId, revision);
        if (!BeanUtils.merge(updateDTO, configurationEntity)) {
            return;
        }
        configurationEntity.setLastModifiedAt(new Date());
        configurationEntity.setLastModifiedBy(operator.getId());
        configurationEntity.updateRevision();
        configurationRepository.save(configurationEntity);
        eventPublisher.publish(this, MailConfigurationUpdatedEvent.class, configurationEntity.getId());
    }

    /**
     * 将配置设置为默认配置。
     * @param operator        操作者信息
     * @param configurationId 配置 ID
     * @param revision        修订版本号
     */
    @Override
    @Transactional
    public void setAsDefault(OperatorDTO operator, String configurationId, Long revision) {
        SmsConfigurationCommandEntity configurationEntity = get(configurationId, revision);
        if (configurationEntity.getIsDefault()) {
            return;
        }
        configurationRepository.unsetDefaults();
        configurationEntity.setIsDefault(true);
        configurationEntity.setLastModifiedAt(new Date());
        configurationEntity.setLastModifiedBy(operator.getId());
        configurationEntity.updateRevision();
        configurationRepository.save(configurationEntity);
    }

    /**
     * 设置配置的停用状态。
     * @param operator        操作者信息
     * @param configurationId 配置 ID
     * @param revision        修订版本号
     */
    @Override
    public void setDisabled(OperatorDTO operator, String configurationId, Long revision, Boolean disabled) {
        SmsConfigurationCommandEntity configurationEntity = get(configurationId, revision);
        if (disabled.equals(configurationEntity.getDisabled())) {
            return;
        }
        configurationEntity.setDisabled(disabled);
        configurationEntity.setLastModifiedAt(new Date());
        configurationEntity.setLastModifiedBy(operator.getId());
        configurationEntity.updateRevision();
        configurationRepository.save(configurationEntity);
    }

    /**
     * 删除配置。
     * @param operator        操作者信息
     * @param configurationId 配置 ID
     * @param revision        修订版本号
     */
    @Override
    public void delete(OperatorDTO operator, String configurationId, Long revision) {
        SmsConfigurationCommandEntity configurationEntity = get(configurationId, revision);
        configurationEntity.setDeleted(true);
        configurationEntity.setDeletedAt(new Date());
        configurationEntity.setDeletedBy(operator.getId());
        configurationEntity.updateRevision();
        configurationRepository.save(configurationEntity);
    }

    /**
     * 取得配置信息。
     * @param configurationId 配置 ID
     * @param revision        修订版本号
     * @return 配置信息
     */
    private SmsConfigurationCommandEntity get(String configurationId, Long revision) {
        SmsConfigurationCommandEntity configurationEntity
            = configurationRepository.findByIdAndDeletedIsFalse(configurationId).orElse(null);
        if (configurationEntity == null) {
            throw new NotFoundError();
        }
        if (revision != null && !revision.equals(configurationEntity.getRevision())) {
            throw new ConflictError();
        }
        if (configurationEntity.getIsDefault()) {
            throw new BusinessError("error.sms.cannot-delete-default-configuration"); // TODO: set message
        }
        return configurationEntity;
    }
}
