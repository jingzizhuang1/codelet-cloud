package net.codelet.cloud.notification.command.listener;

import net.codelet.cloud.event.EventPublisher;
import net.codelet.cloud.notification.command.event.NotificationTemplateCreateEvent;
import net.codelet.cloud.notification.command.event.NotificationTemplateCreatedEvent;
import net.codelet.cloud.notification.command.service.NotificationTemplateCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * 通知模版创建事件监听器。
 */
@Configuration
public class NotificationTemplateCreateEventListener {

    private final EventPublisher eventPublisher;
    private final NotificationTemplateCommandService notificationTemplateCommandService;

    @Autowired
    public NotificationTemplateCreateEventListener(
        EventPublisher eventPublisher,
        NotificationTemplateCommandService notificationTemplateCommandService
    ) {
        this.eventPublisher = eventPublisher;
        this.notificationTemplateCommandService = notificationTemplateCommandService;
    }

    /**
     * 创建通知消息模版。
     * @param event 通知消息模版创建事件
     */
    @EventListener
    public void onNotificationTemplateCreate(NotificationTemplateCreateEvent event) {
        eventPublisher.publish(
            this,
            NotificationTemplateCreatedEvent.class,
            event,
            notificationTemplateCommandService.create(null, event.getPayload())
        );
    }
}
