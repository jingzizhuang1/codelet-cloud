package net.codelet.cloud.notification.command.service.impl;

import net.codelet.cloud.error.BusinessError;
import net.codelet.cloud.notification.command.dto.NotificationEmailSendDTO;
import net.codelet.cloud.notification.command.dto.NotificationSendDTO;
import net.codelet.cloud.notification.command.dto.NotificationSmsSendDTO;
import net.codelet.cloud.notification.command.logic.MailCommandLogic;
import net.codelet.cloud.notification.command.logic.NotificationTemplateCommandLogic;
import net.codelet.cloud.notification.command.logic.SmsCommandLogic;
import net.codelet.cloud.notification.command.service.NotificationCommandService;
import net.codelet.cloud.notification.dto.NotificationTemplateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通知服务。
 */
@Component
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationTemplateCommandLogic notificationTemplateCommandLogic;
    private final MailCommandLogic mailCommandLogic;
    private final SmsCommandLogic smsCommandLogic;

    @Autowired
    public NotificationCommandServiceImpl(
        NotificationTemplateCommandLogic notificationTemplateCommandLogic,
        MailCommandLogic mailCommandLogic,
        SmsCommandLogic smsCommandLogic
    ) {
        this.notificationTemplateCommandLogic = notificationTemplateCommandLogic;
        this.mailCommandLogic = mailCommandLogic;
        this.smsCommandLogic = smsCommandLogic;
    }

    /**
     * 发送电子邮件。
     * @param sendDTO 电子邮件发送数据
     */
    @Override
    public void sendEmail(NotificationEmailSendDTO sendDTO) {
        NotificationTemplateDTO templateDTO = getTemplate(sendDTO);
        mailCommandLogic.send(
            templateDTO.getConfigurationId(),
            templateDTO.renderSubject(sendDTO.getParameters()),
            templateDTO.renderContent(sendDTO.getParameters()),
            templateDTO.isHTML(),
            sendDTO.getEmail()
        );
    }

    /**
     * 发送短信。
     * @param sendDTO 短信发送数据
     */
    @Override
    public void sendSMS(NotificationSmsSendDTO sendDTO) {
        NotificationTemplateDTO templateDTO = getTemplate(sendDTO);
        smsCommandLogic.send(
            templateDTO.getConfigurationId(),
            templateDTO.getContent(),
            sendDTO.getParameters(),
            sendDTO.getMobile()
        );
    }

    /**
     * 根据消息模版及参数渲染消息内容。
     * @param sendDTO 消息数据
     * @return 消息内容
     */
    private NotificationTemplateDTO getTemplate(NotificationSendDTO sendDTO) {
        NotificationTemplateDTO templateDTO = notificationTemplateCommandLogic
            .get(sendDTO.getTemplateId(), sendDTO.getAcceptLanguages());
        if (templateDTO == null) {
            throw new BusinessError("error.notification.no-template");
        }
        return templateDTO;
    }
}
