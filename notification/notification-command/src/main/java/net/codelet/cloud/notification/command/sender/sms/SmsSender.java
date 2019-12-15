package net.codelet.cloud.notification.command.sender.sms;

import java.util.Map;

/**
 * 短信发送工具接口。
 */
public interface SmsSender {

    /**
     * 发送短信。
     * @param content      短信内容
     * @param phoneNumbers 接收者电话号码
     */
    default void send(String content, String... phoneNumbers) {
        send(content, null, phoneNumbers);
    }

    /**
     * 发送短信。
     * @param content      短信内容
     * @param parameters   参数表
     * @param phoneNumbers 接收者电话号码
     */
    void send(String content, Map<String, Object> parameters, String... phoneNumbers);

    /**
     * 取得配置修订版本号。
     * @return 修订版本号
     */
    Long getRevision();
}
