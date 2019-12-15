package net.codelet.cloud.notification.command.logic;

import net.codelet.cloud.notification.dto.NotificationTemplateDTO;

import java.util.List;

/**
 * 消息模版业务逻辑接口。
 */
public interface NotificationTemplateCommandLogic {

    /**
     * 取得模版支持的语言列表。
     * @param templateId 模版 ID
     * @return 支持的语言列表
     */
    List<String> getSupportedLanguages(String templateId);

    /**
     * 取得消息模版。
     * @param templateId          模版 ID
     * @param acceptLanguageCodes 接受的语言代码列表
     * @return 本地化消息模版
     */
    NotificationTemplateDTO get(String templateId, List<String> acceptLanguageCodes);
}
