package net.codelet.cloud.notification.command.event;

import net.codelet.cloud.event.BaseRemoteApplicationEvent;
import net.codelet.cloud.notification.command.entity.NotificationTemplateCommandEntity;
import org.springframework.cloud.bus.BusProperties;

/**
 * 消息模版创建完成事件。
 */
public class NotificationTemplateCreatedEvent extends BaseRemoteApplicationEvent<NotificationTemplateCommandEntity> {

    private static final long serialVersionUID = 1098218856335470969L;

    public NotificationTemplateCreatedEvent() {
    }

    public NotificationTemplateCreatedEvent(
        final BusProperties properties,
        final Object source,
        final NotificationTemplateCommandEntity payload
    ) {
        super(properties, source, payload);
    }
}
