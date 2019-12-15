package net.codelet.cloud.notification.command.service;

import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.notification.command.dto.NotificationTemplateContentDTO;
import net.codelet.cloud.notification.command.dto.NotificationTemplateCreateDTO;
import net.codelet.cloud.notification.command.dto.NotificationTemplateUpdateDTO;
import net.codelet.cloud.notification.command.entity.NotificationTemplateCommandEntity;
import net.codelet.cloud.notification.command.entity.NotificationTemplateContentCommandEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * 通知模版服务接口。
 */
public interface NotificationTemplateCommandService {

    /**
     * 取得模版信息。
     * @param id 模版 ID
     * @return 模版信息
     */
    default NotificationTemplateCommandEntity get(String id) {
        return get(id, false);
    }

    /**
     * 取得模版信息。
     * @param id      模版 ID
     * @param deleted 是否已被删除
     * @return 模版信息
     */
    NotificationTemplateCommandEntity get(String id, Boolean deleted);

    /**
     * 创建通知消息模版。
     * @param operator    操作者信息
     * @param templateDTO 创建表单
     * @return 通知模版信息
     */
    @Transactional
    NotificationTemplateCommandEntity create(
        OperatorDTO operator,
        NotificationTemplateCreateDTO templateDTO
    );

    /**
     * 更新模版信息。
     * @param operator    操作者信息
     * @param templateId  模版 ID
     * @param revision    修订版本号
     * @param templateDTO 模版数据
     */
    @Transactional
    void update(
        OperatorDTO operator,
        String templateId,
        Long revision,
        NotificationTemplateUpdateDTO templateDTO
    );

    /**
     * 停用模版。
     * @param operator   操作者信息
     * @param templateId 模版 ID
     * @param revision   模版数据
     */
    void disable(
        OperatorDTO operator,
        String templateId,
        Long revision
    );

    /**
     * 启用模版。
     * @param operator   操作者信息
     * @param templateId 模版 ID
     * @param revision   模版数据
     */
    void enable(
        OperatorDTO operator,
        String templateId,
        Long revision
    );

    /**
     * 删除模版。
     * @param operator   操作者信息
     * @param templateId 模版 ID
     * @param revision   模版数据
     */
    void delete(
        OperatorDTO operator,
        String templateId,
        Long revision
    );

    /**
     * 保存本地化模版内容数据。
     * @param operator   操作者信息
     * @param templateId 模版 ID
     * @param revision   修订版本号
     * @param contentDTO 内容数据
     * @return 本地化模版内容数据
     */
    NotificationTemplateContentCommandEntity saveLanguageContent(
        OperatorDTO operator,
        String templateId,
        Long revision,
        NotificationTemplateContentDTO contentDTO
    );

    /**
     * 停用模版语言内容。
     * @param operator     操作者信息
     * @param templateId   模版 ID
     * @param languageCode 语言代码
     * @param revision     模版数据
     */
    void disableLanguage(
        OperatorDTO operator,
        String templateId,
        String languageCode,
        Long revision
    );

    /**
     * 启用模版语言内容。
     * @param operator     操作者信息
     * @param templateId   模版 ID
     * @param languageCode 语言代码
     * @param revision     模版数据
     */
    void enableLanguage(
        OperatorDTO operator,
        String templateId,
        String languageCode,
        Long revision
    );

    /**
     * 启用模版语言内容。
     * @param operator     操作者信息
     * @param templateId   模版 ID
     * @param languageCode 语言代码
     * @param revision     模版数据
     */
    void deleteLanguage(
        OperatorDTO operator,
        String templateId,
        String languageCode,
        Long revision
    );
}
