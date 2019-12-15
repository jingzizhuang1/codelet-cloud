package net.codelet.cloud.notification.command.service.impl;

import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.error.BusinessError;
import net.codelet.cloud.error.ConflictError;
import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.notification.command.dto.NotificationTemplateContentDTO;
import net.codelet.cloud.notification.command.dto.NotificationTemplateCreateDTO;
import net.codelet.cloud.notification.command.dto.NotificationTemplateUpdateDTO;
import net.codelet.cloud.notification.command.entity.NotificationTemplateCommandEntity;
import net.codelet.cloud.notification.command.entity.NotificationTemplateContentCommandEntity;
import net.codelet.cloud.notification.command.repository.NotificationTemplateCommandRepository;
import net.codelet.cloud.notification.command.repository.NotificationTemplateContentCommandRepository;
import net.codelet.cloud.notification.command.service.NotificationTemplateCommandService;
import net.codelet.cloud.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static net.codelet.cloud.constant.EntityIDs.SYSTEM_USER_ID;

/**
 * 通知模版服务。
 */
@Component
public class NotificationTemplateCommandServiceImpl implements NotificationTemplateCommandService {

    private final NotificationTemplateCommandRepository notificationCommandRepository;
    private final NotificationTemplateContentCommandRepository notificationTemplateContentCommandRepository;

    @Autowired
    public NotificationTemplateCommandServiceImpl(
        NotificationTemplateCommandRepository notificationCommandRepository,
        NotificationTemplateContentCommandRepository notificationTemplateContentCommandRepository
    ) {
        this.notificationCommandRepository = notificationCommandRepository;
        this.notificationTemplateContentCommandRepository = notificationTemplateContentCommandRepository;
    }

    /**
     * 取得模版信息。
     * @param id      模版 ID
     * @param deleted 是否已被删除
     * @return 模版信息
     */
    @Override
    public NotificationTemplateCommandEntity get(String id, Boolean deleted) {
        if (deleted == null) {
            return notificationCommandRepository.findById(id).orElse(null);
        }
        return notificationCommandRepository.findByIdAndDeletedIs(id, deleted).orElse(null);
    }

    /**
     * 取得模版信息。
     * @param id       模版 ID
     * @param revision 修订版本号
     * @return 模版信息
     */
    private NotificationTemplateCommandEntity get(String id, Long revision) {
        NotificationTemplateCommandEntity templateEntity = notificationCommandRepository
            .findByIdAndDeletedIs(id, false)
            .orElse(null);
        if (templateEntity == null) {
            throw new NotFoundError();
        }
        if (!templateEntity.getRevision().equals(revision)) {
            throw new ConflictError();
        }
        return templateEntity;
    }

    /**
     * 创建通知消息模版。
     * @param operator    操作者信息
     * @param templateDTO 创建表单
     * @return 通知模版信息
     */
    @Override
    @Transactional
    public NotificationTemplateCommandEntity create(
        final OperatorDTO operator,
        final NotificationTemplateCreateDTO templateDTO
    ) {
        // 保存模版信息
        NotificationTemplateCommandEntity templateEntity = new NotificationTemplateCommandEntity();
        BeanUtils.copyProperties(templateDTO, templateEntity);
        templateEntity.setCreatedAt(new Date());
        templateEntity.setCreatedBy(operator == null ? SYSTEM_USER_ID : operator.getId());
        templateEntity.updateRevision();
        notificationCommandRepository.save(templateEntity);

        // 保存模版本地化内容数据
        for (NotificationTemplateContentDTO contentDTO : templateDTO.getContents()) {
            saveLanguageContent(operator, templateEntity.getId(), false, null, contentDTO);
        }

        return templateEntity;
    }

    /**
     * 更新模版信息。
     * @param operator    操作者信息
     * @param templateId  模版 ID
     * @param revision    修订版本号
     * @param templateDTO 模版数据
     */
    @Override
    @Transactional
    public void update(
        final OperatorDTO operator,
        final String templateId,
        final Long revision,
        final NotificationTemplateUpdateDTO templateDTO
    ) {
        // 尝试取得模版信息
        NotificationTemplateCommandEntity templateEntity = get(templateId, revision);
        if (!BeanUtils.merge(templateDTO, templateEntity)) {
            return;
        }

        // 保存模版信息
        templateEntity.setLastModifiedAt(new Date());
        templateEntity.setLastModifiedBy(operator.getId());
        templateEntity.updateRevision();
        notificationCommandRepository.save(templateEntity);

        // 保存模版本地化内容数据
        for (NotificationTemplateContentDTO contentDTO : templateDTO.getContents()) {
            saveLanguageContent(operator, templateId, false, null, contentDTO);
        }
    }

    /**
     * 停用模版。
     * @param operator   操作者信息
     * @param templateId 模版 ID
     * @param revision   模版数据
     */
    @Override
    public void disable(
        final OperatorDTO operator,
        final String templateId,
        final Long revision
    ) {
        NotificationTemplateCommandEntity templateEntity = get(templateId, revision);

        if (templateEntity.getDisabled() != null && templateEntity.getDisabled()) {
            return;
        }

        Date timestamp = new Date();
        templateEntity.setLastModifiedAt(timestamp);
        templateEntity.setLastModifiedBy(operator.getId());
        templateEntity.setDisabled(true);
        templateEntity.setDisabledAt(timestamp);
        templateEntity.setDisabledBy(operator.getId());
        templateEntity.updateRevision();
        notificationCommandRepository.save(templateEntity);
    }

    /**
     * 启用模版。
     * @param operator   操作者信息
     * @param templateId 模版 ID
     * @param revision   模版数据
     */
    @Override
    public void enable(
        final OperatorDTO operator,
        final String templateId,
        final Long revision
    ) {
        NotificationTemplateCommandEntity templateEntity = get(templateId, revision);

        if (templateEntity.getDisabled() == null || !templateEntity.getDisabled()) {
            return;
        }

        templateEntity.setLastModifiedAt(new Date());
        templateEntity.setLastModifiedBy(operator.getId());
        templateEntity.setDisabled(false);
        templateEntity.updateRevision();
        notificationCommandRepository.save(templateEntity);
    }

    /**
     * 删除模版。
     * @param operator   操作者信息
     * @param templateId 模版 ID
     * @param revision   模版数据
     */
    @Override
    public void delete(
        final OperatorDTO operator,
        final String templateId,
        final Long revision
    ) {
        NotificationTemplateCommandEntity templateEntity = get(templateId, revision);
        templateEntity.setDeleted(true);
        templateEntity.setDeletedAt(new Date());
        templateEntity.setDeletedBy(operator.getId());
        templateEntity.updateRevision();
        notificationCommandRepository.save(templateEntity);
    }

    /**
     * 保存本地化模版内容数据。
     * @param operator   操作者信息
     * @param templateId 模版 ID
     * @param revision   修订版本号
     * @param contentDTO 内容数据
     * @return 本地化模版内容数据
     */
    @Override
    public NotificationTemplateContentCommandEntity saveLanguageContent(
        final OperatorDTO operator,
        final String templateId,
        final Long revision,
        final NotificationTemplateContentDTO contentDTO
    ) {
        return saveLanguageContent(operator, templateId, true, revision, contentDTO);
    }

    /**
     * 保存本地化模版内容数据。
     * @param operator         操作者信息
     * @param templateId       模版 ID
     * @param validateRevision 是否检查修订版本号
     * @param revision         修订版本号
     * @param contentDTO       内容数据
     * @return 本地化模版内容数据
     */
    private NotificationTemplateContentCommandEntity saveLanguageContent(
        final OperatorDTO operator,
        final String templateId,
        final Boolean validateRevision,
        final Long revision,
        final NotificationTemplateContentDTO contentDTO
    ) {
        // 尝试取得已设置的语言内容
        NotificationTemplateContentCommandEntity contentEntity = notificationTemplateContentCommandRepository
            .findByTemplateIdAndLanguageCodeAndDeletedIsFalse(templateId, contentDTO.getLanguageCode())
            .orElse(null);

        // 检查修订版本号是否一致
        if (validateRevision
            && (
                (revision != null && (contentEntity == null || !revision.equals(contentEntity.getRevision())))
                || (revision == null && contentEntity != null)
            )
        ) {
            throw new ConflictError();
        }

        if (contentEntity == null) {
            // 新建内容时构造内容实体实例
            contentEntity = new NotificationTemplateContentCommandEntity();
            // 检查内容所属模版是否存在
            if (validateRevision && get(templateId, false) == null) {
                throw new BusinessError("error.notification.no-template");
            }
        }

        // 保存语言内容
        BeanUtils.copyProperties(contentDTO, contentEntity);
        contentEntity.setTemplateId(templateId);
        contentEntity.setCreatedAt(new Date());
        contentEntity.setCreatedBy(operator == null ? SYSTEM_USER_ID : operator.getId());
        contentEntity.updateRevision();
        return notificationTemplateContentCommandRepository.save(contentEntity);
    }

    /**
     * 取得模版语言内容。
     * @param templateId   模版 ID
     * @param languageCode 语言代码
     * @param revision     修订版本号
     * @return 模版语言内容
     */
    private NotificationTemplateContentCommandEntity getLanguageContent(
        final String templateId,
        final String languageCode,
        final Long revision
    ) {
        NotificationTemplateContentCommandEntity contentEntity = notificationTemplateContentCommandRepository
            .findByTemplateIdAndLanguageCodeAndDeletedIsFalse(templateId, languageCode).orElse(null);
        if (contentEntity == null) {
            throw new NotFoundError();
        }
        if (!contentEntity.getRevision().equals(revision)) {
            throw new ConflictError();
        }
        return contentEntity;
    }

    /**
     * 停用模版语言内容。
     * @param operator     操作者信息
     * @param templateId   模版 ID
     * @param languageCode 语言代码
     * @param revision     模版数据
     */
    @Override
    public void disableLanguage(
        final OperatorDTO operator,
        final String templateId,
        final String languageCode,
        final Long revision
    ) {
        NotificationTemplateContentCommandEntity contentEntity
            = getLanguageContent(templateId, languageCode, revision);

        if (contentEntity.getDisabled() != null && contentEntity.getDisabled()) {
            return;
        }

        Date timestamp = new Date();
        contentEntity.setLastModifiedAt(timestamp);
        contentEntity.setLastModifiedBy(operator.getId());
        contentEntity.setDisabled(true);
        contentEntity.setDisabledAt(timestamp);
        contentEntity.setDisabledBy(operator.getId());
        contentEntity.updateRevision();
        notificationTemplateContentCommandRepository.save(contentEntity);
    }

    /**
     * 启用模版语言内容。
     * @param operator     操作者信息
     * @param templateId   模版 ID
     * @param languageCode 语言代码
     * @param revision     模版数据
     */
    @Override
    public void enableLanguage(
        final OperatorDTO operator,
        final String templateId,
        final String languageCode,
        final Long revision
    ) {
        NotificationTemplateContentCommandEntity contentEntity
            = getLanguageContent(templateId, languageCode, revision);

        if (contentEntity.getDisabled() == null || !contentEntity.getDisabled()) {
            return;
        }

        contentEntity.setLastModifiedAt(new Date());
        contentEntity.setLastModifiedBy(operator.getId());
        contentEntity.setDisabled(false);
        contentEntity.updateRevision();
        notificationTemplateContentCommandRepository.save(contentEntity);
    }

    /**
     * 启用模版语言内容。
     * @param operator     操作者信息
     * @param templateId   模版 ID
     * @param languageCode 语言代码
     * @param revision     模版数据
     */
    @Override
    public void deleteLanguage(
        final OperatorDTO operator,
        final String templateId,
        final String languageCode,
        final Long revision
    ) {
        NotificationTemplateContentCommandEntity contentEntity
            = getLanguageContent(templateId, languageCode, revision);
        contentEntity.setDeleted(true);
        contentEntity.setDeletedAt(new Date());
        contentEntity.setDeletedBy(operator.getId());
        contentEntity.updateRevision();
        notificationTemplateContentCommandRepository.save(contentEntity);
    }
}
