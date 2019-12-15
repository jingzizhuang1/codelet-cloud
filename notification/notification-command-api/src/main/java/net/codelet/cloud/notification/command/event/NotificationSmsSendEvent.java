package net.codelet.cloud.notification.command.event;

import net.codelet.cloud.event.BaseRemoteApplicationEvent;
import net.codelet.cloud.notification.command.dto.NotificationSmsSendDTO;

/**
 * 通知短信发送事件。
 */
public class NotificationSmsSendEvent extends BaseRemoteApplicationEvent<NotificationSmsSendDTO> {
    private static final long serialVersionUID = -5429663883320957274L;
}
