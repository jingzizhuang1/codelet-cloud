package net.codelet.cloud.notification.command.logic.impl;

import net.codelet.cloud.error.BusinessError;
import net.codelet.cloud.error.ValidationError;
import net.codelet.cloud.notification.command.entity.MailConfigurationCommandEntity;
import net.codelet.cloud.notification.command.logic.MailCommandLogic;
import net.codelet.cloud.notification.command.repository.MailConfigurationCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件发送服务。
 */
@Component
public class MailCommandLogicImpl implements MailCommandLogic {

    private static final Map<String, JavaMailSender> senders = new HashMap<>();
    private static final Map<String, InternetAddress> senderFroms = new HashMap<>();
    private static final String DEFAULT_CONFIGURATION_KEY = "DEFAULT";

    private final MailConfigurationCommandRepository configurationRepository;

    @Autowired
    public MailCommandLogicImpl(
        MailConfigurationCommandRepository configurationRepository
    ) {
        this.configurationRepository = configurationRepository;
    }

    /**
     * 更新 Java Mail Sender 实例。
     * @param configurationId 配置 ID
     */
    @Override
    public void updateSender(String configurationId) {
        senders.remove(configurationId);
        senderFroms.remove(configurationId);
        getSender(configurationId);
    }

    /**
     * 发送邮件。
     * @param configurationId 配置 ID
     * @param from            发送者邮件地址
     * @param subject         标题
     * @param content         内容
     * @param html            是否为 HTML
     * @param to              接收者邮件地址列表
     */
    @Override
    public void send(String configurationId, InternetAddress from, String subject, String content, boolean html, InternetAddress... to) {
        JavaMailSender sender = getSender(configurationId);

        if (from == null) {
            from = senderFroms.get(configurationId);
        }

        MimeMessage message = sender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, html);
        } catch (MessagingException e) {
            throw new ValidationError("error.mail.invalid-message"); // TODO: message
        }

        sender.send(message);
    }

    /**
     * 取得邮件发送工具实例。
     * @param configurationId 配置 ID
     * @return 发送工具实例
     */
    private JavaMailSender getSender(String configurationId) {
        // 尝试取得已生成的电子邮件发送工具实例
        JavaMailSenderImpl sender = (JavaMailSenderImpl) senders
            .get(configurationId == null ? DEFAULT_CONFIGURATION_KEY : configurationId);

        if (sender != null) {
            return sender;
        }

        // 取得配置信息
        MailConfigurationCommandEntity configuration = (
            configurationId == null
                ? configurationRepository.findByIsDefaultIsTrueAndDeletedIsFalse()
                : configurationRepository.findByIdAndDeletedIsFalse(configurationId)
        ).orElse(null);
        if (configuration == null) {
            throw new BusinessError("error.email.configuration-not-found"); // TODO: set message
        }

        // 构造邮件发送工具实例
        sender = new JavaMailSenderImpl();
        sender.setHost(configuration.getHost());
        sender.setPort(configuration.getPort());
        sender.setProtocol(configuration.getProtocol());
        sender.setUsername(configuration.getUsername());
        sender.setPassword(configuration.getPassword());
        sender.setDefaultEncoding("UTF-8");
        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", configuration.getPort().toString());
        properties.put("mail.smtp.starttls.enable", configuration.getStartTlsEnabled().toString());
        properties.put("mail.smtp.starttls.required", configuration.getStartTlsRequired().toString());
        properties.put("mail.smtp.socketFactory.port", configuration.getPort().toString());
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.connectiontimeout", configuration.getConnectionTimeoutMilliseconds().toString());
        properties.put("mail.smtp.timeout", configuration.getReadTimeoutMilliseconds().toString());
        properties.put("mail.smtp.writetimeout", configuration.getWriteTimeoutMilliseconds().toString());
        sender.setJavaMailProperties(properties);

        InternetAddress senderFrom;
        try {
            senderFrom = new InternetAddress(configuration.getUsername(), configuration.getSenderName());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(System.err);
            throw new ValidationError("error.email.username-is-invalid"); // TODO: set message
        }

        // 缓存生成的发送工具
        senders.put(configurationId, sender);
        senderFroms.put(configurationId, senderFrom);
        if (configurationId == null) {
            senders.put(DEFAULT_CONFIGURATION_KEY, sender);
            senderFroms.put(DEFAULT_CONFIGURATION_KEY, senderFrom);
        }

        return sender;
    }
}
