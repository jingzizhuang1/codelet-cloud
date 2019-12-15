package net.codelet.cloud.notification.command.event;

import net.codelet.cloud.event.BaseRemoteApplicationEvent;
import net.codelet.cloud.notification.command.dto.NotificationEmailSendDTO;

/**
 * 通知邮件发送事件。
 */
public class NotificationEmailSendEvent extends BaseRemoteApplicationEvent<NotificationEmailSendDTO> {
    private static final long serialVersionUID = -312379390440866070L;
}
