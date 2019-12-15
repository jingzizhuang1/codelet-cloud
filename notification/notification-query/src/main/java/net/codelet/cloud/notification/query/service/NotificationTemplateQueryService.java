package net.codelet.cloud.notification.query.service;

import net.codelet.cloud.notification.query.dto.NotificationTemplateGetDTO;
import net.codelet.cloud.notification.query.dto.NotificationTemplateQueryDTO;
import net.codelet.cloud.notification.query.entity.NotificationTemplateQueryEntity;
import net.codelet.cloud.notification.query.entity.NotificationTemplateWithContentsQueryEntity;
import org.springframework.data.domain.Page;

/**
 * 通知消息模版查询服务接口。
 */
public interface NotificationTemplateQueryService {

    /**
     * 查询通知模版。
     * @param queryDTO 查询条件
     * @return 通知模版分页数据
     */
    Page<NotificationTemplateQueryEntity> search(
        NotificationTemplateQueryDTO queryDTO
    );

    /**
     * 取得通知消息模版。
     * @param templateId 模版 ID
     * @param queryDTO   查询过滤条件
     * @return 通知消息模版
     */
    NotificationTemplateWithContentsQueryEntity get(
        String templateId,
        NotificationTemplateGetDTO queryDTO
    );

    /**
     * 检查模版是否存在。
     * @param templateId 模版 ID
     * @param disabled   是否已被停用
     * @return 检查结果
     */
    boolean exists(String templateId, Boolean disabled);
}
