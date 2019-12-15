package net.codelet.cloud.notification.command.repository;

import net.codelet.cloud.notification.command.entity.NotificationTemplateContentAvailableCommandEntity;
import net.codelet.cloud.repository.BaseRepository;

import java.util.Optional;

public interface NotificationTemplateContentAvailableCommandRepository extends BaseRepository<NotificationTemplateContentAvailableCommandEntity> {

    /**
     * 查询使用中的消息模版内容。
     * @param templateId   模版 ID
     * @param languageCode 语言代码
     * @return 本地化模版内容
     */
    Optional<NotificationTemplateContentAvailableCommandEntity> findByTemplateIdAndLanguageCode(String templateId, String languageCode);
}
