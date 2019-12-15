package net.codelet.cloud.notification.command.api;

import net.codelet.cloud.notification.command.dto.NotificationTemplateContentSaveDTO;
import net.codelet.cloud.notification.command.dto.NotificationTemplateCreateDTO;
import net.codelet.cloud.notification.command.dto.NotificationTemplateUpdateDTO;
import net.codelet.cloud.notification.command.entity.NotificationTemplateCommandEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "${services.notification.command.name:notification-command}",
    contextId = "notification-template-command"
)
public interface NotificationTemplateCommandApi {

    /**
     * 创建通知消息模版。
     * @param createDTO 创建表单
     * @return 通知消息模版信息
     */
    @PostMapping("/notification-templates")
    NotificationTemplateCommandEntity create(
        @RequestBody NotificationTemplateCreateDTO createDTO
    );

    /**
     * 更新通知消息模版。
     * @param templateId 模版 ID
     * @param revision   修订版本号
     * @param updateDTO  模版数据
     */
    @PatchMapping("/notification-templates/{templateId}")
    void update(
        @PathVariable("templateId") String templateId,
        @RequestParam("revision") long revision,
        @RequestBody NotificationTemplateUpdateDTO updateDTO
    );

    /**
     * 停用通知消息模版。
     * @param templateId 模版 ID
     * @param revision   修订版本号
     */
    @PostMapping("/notification-templates/{templateId}/disable")
    void disable(
        @PathVariable("templateId") String templateId,
        @RequestParam("revision") long revision
    );

    /**
     * 启用通知消息模版。
     * @param templateId 模版 ID
     * @param revision   修订版本号
     */
    @PostMapping("/notification-templates/{templateId}/enable")
    void enable(
        @PathVariable("templateId") String templateId,
        @RequestParam("revision") long revision
    );

    /**
     * 删除通知消息模版。
     * @param templateId 模版 ID
     * @param revision   修订版本号
     */
    @DeleteMapping("/notification-templates/{templateId}")
    void delete(
        @PathVariable("templateId") String templateId,
        @RequestParam("revision") long revision
    );

    /**
     * 设置通知消息模版语言内容。
     * @param templateId     模版 ID
     * @param languageCode   语言代码
     * @param revision       修订版本号
     * @param contentSaveDTO 本地化语言内容数据
     */
    @PutMapping("/notification-templates/{templateId}/languages/{languageCode}")
    void setLanguage(
        @PathVariable("templateId") String templateId,
        @PathVariable("languageCode") String languageCode,
        @RequestParam(name = "revision", required = false) long revision,
        @RequestBody NotificationTemplateContentSaveDTO contentSaveDTO
    );

    /**
     * 停用通知消息模版语言内容。
     * @param templateId   模版 ID
     * @param languageCode 语言代码
     * @param revision     修订版本号
     */
    @PostMapping("/notification-templates/{templateId}/languages/{languageCode}/disable")
    void disableLanguage(
        @PathVariable("templateId") String templateId,
        @PathVariable("languageCode") String languageCode,
        @RequestParam("revision") long revision
    );

    /**
     * 启用通知消息模版语言内容。
     * @param templateId   模版 ID
     * @param languageCode 语言代码
     * @param revision     修订版本号
     */
    @PostMapping("/notification-templates/{templateId}/languages/{languageCode}/enable")
    void enableLanguage(
        @PathVariable("templateId") String templateId,
        @PathVariable("languageCode") String languageCode,
        @RequestParam("revision") long revision
    );

    /**
     * 删除通知消息模版语言内容。
     * @param templateId   模版 ID
     * @param languageCode 语言代码
     * @param revision     修订版本号
     */
    @DeleteMapping("/notification-templates/{templateId}/languages/{languageCode}")
    void deleteLanguage(
        @PathVariable("templateId") String templateId,
        @PathVariable("languageCode") String languageCode,
        @RequestParam("revision") long revision
    );
}
