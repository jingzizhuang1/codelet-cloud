package net.codelet.cloud.notification.command.logic;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * 邮件发送服务接口。
 */
public interface MailCommandLogic {

    /**
     * 更新 Java Mail Sender 实例。
     * @param configurationId 配置 ID
     */
    void updateSender(String configurationId);

    /**
     * 发送邮件。
     * @param subject 标题
     * @param content 内容
     * @param html    是否为 HTML
     * @param targets 接收者邮件地址列表
     */
    default void send(String subject, String content, boolean html, String... targets) {
        send(null, subject, content, html, targets);
    }

    /**
     * 发送邮件。
     * @param configurationId 配置 ID
     * @param subject         标题
     * @param content         内容
     * @param html            是否为 HTML
     * @param targets         接收者邮件地址列表
     */
    default void send(String configurationId, String subject, String content, boolean html, String... targets) {
        List<InternetAddress> to = new ArrayList<>();
        for (String target : targets) {
            try {
                to.add(new InternetAddress(target));
            } catch (AddressException e) {
                e.printStackTrace(System.err);
            }
        }
        send(configurationId, null, subject, content, html, to.toArray(new InternetAddress[]{}));
    }

    /**
     * 发送邮件。
     * @param subject 标题
     * @param content 内容
     * @param html    是否为 HTML
     * @param to      接收者邮件地址列表
     */
    default void send(String subject, String content, boolean html, InternetAddress... to) {
        send(null, null, subject, content, html, to);
    }

    /**
     * 发送邮件。
     * @param configurationId 配置 ID
     * @param subject         标题
     * @param content         内容
     * @param html            是否为 HTML
     * @param to              接收者邮件地址列表
     */
    default void send(String configurationId, String subject, String content, boolean html, InternetAddress... to) {
        send(configurationId, null, subject, content, html, to);
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
    void send(String configurationId, InternetAddress from, String subject, String content, boolean html, InternetAddress... to);
}
