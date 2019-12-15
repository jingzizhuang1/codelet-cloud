package net.codelet.cloud.notification.command.repository;

import net.codelet.cloud.notification.command.entity.NotificationTemplateCommandEntity;
import net.codelet.cloud.repository.BaseRepository;

import java.util.Optional;

/**
 * 通知消息模版数据仓库。
 */
public interface NotificationTemplateCommandRepository extends BaseRepository<NotificationTemplateCommandEntity> {

    /**
     * 取得模版信息。
     * @param id      模版 ID
     * @param deleted 是否已被删除
     * @return 模版信息
     */
    Optional<NotificationTemplateCommandEntity> findByIdAndDeletedIs(String id, Boolean deleted);
}
