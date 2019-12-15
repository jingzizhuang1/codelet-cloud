package net.codelet.cloud.notification.query.repository;

import net.codelet.cloud.notification.query.entity.NotificationTemplateContentQueryEntity;
import net.codelet.cloud.repository.BaseRepository;

import java.util.Optional;

public interface NotificationTemplateContentQueryRepository extends BaseRepository<NotificationTemplateContentQueryEntity> {

    Optional<NotificationTemplateContentQueryEntity> findByTemplateIdAndLanguageCodeAndDeletedIsFalse(String templateId, String languageCode);
}
