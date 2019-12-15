package net.codelet.cloud.notification.command.listener;

import net.codelet.cloud.notification.command.event.MailConfigurationUpdatedEvent;
import net.codelet.cloud.notification.command.logic.MailCommandLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * 邮件配置更新事件监听器。
 */
@Configuration
public class MailConfigurationUpdatedEventListener {

    private final MailCommandLogic mailCommandLogic;

    @Autowired
    public MailConfigurationUpdatedEventListener(MailCommandLogic mailCommandLogic) {
        this.mailCommandLogic = mailCommandLogic;
    }

    /**
     * 更新本地邮件配置。
     */
    @EventListener
    public void onMailConfigurationUpdated(MailConfigurationUpdatedEvent event) {
        mailCommandLogic.updateSender(event.getPayload());
    }
}
