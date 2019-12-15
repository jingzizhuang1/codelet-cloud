package net.codelet.cloud.notification.command.service;

import net.codelet.cloud.notification.command.dto.NotificationEmailSendDTO;
import net.codelet.cloud.notification.command.dto.NotificationSmsSendDTO;

/**
 * 通知服务接口。
 */
public interface NotificationCommandService {

    /**
     * 发送电子邮件。
     * @param sendDTO 电子邮件发送数据
     */
    void sendEmail(NotificationEmailSendDTO sendDTO);

    /**
     * 发送短信。
     * @param sendDTO 短信发送数据
     */
    void sendSMS(NotificationSmsSendDTO sendDTO);
}
