package net.codelet.cloud.notification.query.service;

import net.codelet.cloud.notification.query.dto.NotificationTemplateContentQueryDTO;
import net.codelet.cloud.notification.query.entity.NotificationTemplateContentQueryEntity;
import org.springframework.data.domain.Page;

/**
 * 通知消息模版查询服务接口。
 */
public interface NotificationTemplateContentQueryService {

    /**
     * 查询通知模版。
     * @param templateId 模版 ID
     * @param queryDTO   查询条件
     * @return 通知模版分页数据
     */
    Page<NotificationTemplateContentQueryEntity> search(
        String templateId,
        NotificationTemplateContentQueryDTO queryDTO
    );

    /**
     * 取得通知消息模版。
     * @param templateId   模版 ID
     * @param languageCode 语言代码
     * @return 通知消息模版
     */
    NotificationTemplateContentQueryEntity get(String templateId, String languageCode);
}
