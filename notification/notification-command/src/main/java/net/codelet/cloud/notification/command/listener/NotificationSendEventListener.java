package net.codelet.cloud.notification.command.listener;

import net.codelet.cloud.notification.command.event.NotificationEmailSendEvent;
import net.codelet.cloud.notification.command.event.NotificationSmsSendEvent;
import net.codelet.cloud.notification.command.service.NotificationCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * 通知发送事件监听器。
 */
@Configuration
public class NotificationSendEventListener {

    private final NotificationCommandService notificationCommandService;

    @Autowired
    public NotificationSendEventListener(NotificationCommandService notificationCommandService) {
        this.notificationCommandService = notificationCommandService;
    }

    /**
     * 发送邮件通知。
     * @param event 邮件通知发送事件
     */
    @EventListener
    public void onSendEmail(NotificationEmailSendEvent event) {
        notificationCommandService.sendEmail(event.getPayload());
    }

    /**
     * 发送短信通知。
     * @param event 短信通知发送事件
     */
    @EventListener
    public void onSendSms(NotificationSmsSendEvent event) {
        notificationCommandService.sendSMS(event.getPayload());
    }
}
