package net.codelet.cloud.notification.command.event;

import net.codelet.cloud.event.BaseBroadcastEvent;

/**
 * 邮件配置更新事件。
 */
public class MailConfigurationUpdatedEvent extends BaseBroadcastEvent<String> {
    private static final long serialVersionUID = 6697701828857674635L;
}
