package net.codelet.cloud.notification.query.api;

import net.codelet.cloud.notification.query.dto.NotificationTemplateContentQueryDTO;
import net.codelet.cloud.notification.query.entity.NotificationTemplateContentQueryEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "${services.notification.query.name:notification-query}",
    contextId = "notification-template-content-query"
)
public interface NotificationTemplateContentQueryApi {

    /**
     * 查询通知消息模版内容。
     * @param templateId 模版 ID
     * @param queryDTO   过滤条件
     * @return 通知消息模版内容分页数据
     */
    @GetMapping("/notification-templates/{templateId}/languages")
    Page<NotificationTemplateContentQueryEntity> search(
        @PathVariable("templateId") String templateId,
        NotificationTemplateContentQueryDTO queryDTO
    );

    /**
     * 取得通知消息模版内容详细信息。
     * @param templateId   模版 ID
     * @param languageCode 语言代码
     * @return 通知消息模版内容详细信息
     */
    @GetMapping("/notification-templates/{templateId}/languages/{languageCode}")
    NotificationTemplateContentQueryEntity get(
        @PathVariable("templateId") String templateId,
        @PathVariable("languageCode") String languageCode
    );
}
