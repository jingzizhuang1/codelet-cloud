package net.codelet.cloud.notification.command.logic;

import java.util.Map;

/**
 * 短信发送服务接口。
 */
public interface SmsCommandLogic {

    /**
     * 更新短信发送工具。
     * @param configurationId 发送配置 ID
     */
    void updateSender(String configurationId);

    /**
     * 发送短信（使用默认配置）。
     * @param content      短信内容或服务提供方的短信模版 ID
     * @param phoneNumbers 接收者手机号码
     */
    default void send(String content, String... phoneNumbers) {
        send(null, content, null, phoneNumbers);
    }

    /**
     * 发送短信（使用默认配置）。
     * @param content         短信内容或服务提供方的短信模版 ID
     * @param parameters      内容参数
     * @param phoneNumbers    接收者手机号码
     */
    default void send(String content, Map<String, Object> parameters, String... phoneNumbers) {
        send(null, content, parameters, phoneNumbers);
    }

    /**
     * 发送短信。
     * @param configurationId 发送配置 ID
     * @param content         短信内容或服务提供方的短信模版 ID
     * @param phoneNumbers    接收者手机号码
     */
    default void send(String configurationId, String content, String... phoneNumbers) {
        send(configurationId, content, null, phoneNumbers);
    }

    /**
     * 发送短信。
     * @param configurationId 发送配置 ID
     * @param content         短信内容或服务提供方的短信模版 ID
     * @param parameters      内容参数
     * @param phoneNumbers    接收者手机号码
     */
    void send(String configurationId, String content, Map<String, Object> parameters, String... phoneNumbers);
}
