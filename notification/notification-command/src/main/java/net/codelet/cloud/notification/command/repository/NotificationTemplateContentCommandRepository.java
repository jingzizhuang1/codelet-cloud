package net.codelet.cloud.notification.command.repository;

import net.codelet.cloud.notification.command.entity.NotificationTemplateContentCommandEntity;
import net.codelet.cloud.repository.BaseRepository;

import java.util.Optional;

public interface NotificationTemplateContentCommandRepository extends BaseRepository<NotificationTemplateContentCommandEntity> {

    /**
     * 根据模版 ID 及语言代码取得本地化模版内容。
     * @param templateId   模版 ID
     * @param languageCode 语言代码
     * @return 本地化模版内容
     */
    Optional<NotificationTemplateContentCommandEntity> findByTemplateIdAndLanguageCodeAndDeletedIsFalse(String templateId, String languageCode);
}
