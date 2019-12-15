package net.codelet.cloud.notification.query.api;

import net.codelet.cloud.notification.query.dto.NotificationTemplateGetDTO;
import net.codelet.cloud.notification.query.dto.NotificationTemplateQueryDTO;
import net.codelet.cloud.notification.query.entity.NotificationTemplateQueryEntity;
import net.codelet.cloud.notification.query.entity.NotificationTemplateWithContentsQueryEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "${services.notification.query.name:notification-query}",
    contextId = "notification-template-query"
)
public interface NotificationTemplateQueryApi {

    /**
     * 查询通知消息模版。
     * @return 通知消息模版详细信息
     */
    @GetMapping("/notification-templates")
    Page<NotificationTemplateQueryEntity> search(
        NotificationTemplateQueryDTO queryDTO
    );

    /**
     * 取得通知消息模版详细信息。
     * @param templateId 模版 ID
     * @param queryDTO   过滤条件
     * @return 通知消息模版详细信息
     */
    @GetMapping("/notification-templates/{templateId}")
    NotificationTemplateWithContentsQueryEntity get(
        @PathVariable("templateId") String templateId,
        NotificationTemplateGetDTO queryDTO
    );

    /**
     * 检查模版是否存在。
     * @param templateId 模版 ID
     * @param disabled   是否已被停用
     * @return 检查结果
     */
    @GetMapping("/notification-templates/{templateId}/exists")
    boolean exists(
        @PathVariable("templateId") String templateId,
        @RequestParam(value = "disabled", required = false) Boolean disabled
    );
}
