package net.codelet.cloud.notification.command.event;

import net.codelet.cloud.event.BaseRemoteApplicationEvent;
import net.codelet.cloud.notification.command.dto.NotificationTemplateCreateDTO;

/**
 * 消息模版创建事件。
 */
public class NotificationTemplateCreateEvent extends BaseRemoteApplicationEvent<NotificationTemplateCreateDTO> {
    private static final long serialVersionUID = 8009637504266094118L;
}
