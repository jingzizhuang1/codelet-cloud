package net.codelet.cloud.notification.command.logic.impl;

import net.codelet.cloud.error.BusinessError;
import net.codelet.cloud.notification.command.entity.SmsConfigurationCommandEntity;
import net.codelet.cloud.notification.command.logic.SmsCommandLogic;
import net.codelet.cloud.notification.command.repository.SmsConfigurationCommandRepository;
import net.codelet.cloud.notification.command.sender.sms.AliYunSmsSender;
import net.codelet.cloud.notification.command.sender.sms.SmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送服务。
 */
@Component
public class SmsCommandLogicImpl implements SmsCommandLogic {

    private static final Map<String, SmsSender> senders = new HashMap<>();
    private static final String DEFAULT_CONFIGURATION_KEY = "DEFAULT";

    private final SmsConfigurationCommandRepository configurationRepository;

    @Autowired
    public SmsCommandLogicImpl(SmsConfigurationCommandRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    /**
     * 更新短信发送工具。
     * @param configurationId 发送配置 ID
     */
    @Override
    public void updateSender(String configurationId) {
        senders.remove(configurationId);
        getSender(configurationId);
    }

    /**
     * 发送短信。
     * @param configurationId 发送配置 ID
     * @param content         短信内容或服务提供方的短信模版 ID
     * @param parameters      内容参数
     * @param phoneNumbers    接收者手机号码
     */
    @Override
    public void send(String configurationId, String content, Map<String, Object> parameters, String... phoneNumbers) {
        getSender(configurationId).send(content, parameters, phoneNumbers);
    }

    /**
     * 取得短信发送工具实例。
     * @param configurationId 发送配置 ID
     * @return 短信发送工具实例
     */
    private SmsSender getSender(String configurationId) {
        // 尝试取得已生成的短信发送工具实例
        SmsSender sender = senders
            .get(configurationId == null ? DEFAULT_CONFIGURATION_KEY : configurationId);
        if (sender != null) {
            return sender;
        }

        // 取得配置信息
        SmsConfigurationCommandEntity configuration = (
            configurationId == null
                ? configurationRepository.findByIsDefaultIsTrueAndDeletedIsFalse()
                : configurationRepository.findByIdAndDeletedIsFalse(configurationId)).orElse(null);
        if (configuration == null) {
            throw new BusinessError("error.sms.configuration-not-found"); // TODO: set message
        }

        // 生成发送工具实例
        switch (configuration.getProvider()) {
            case ALI_YUN:
                sender = new AliYunSmsSender(configuration);
                break;
        }

        // 缓存生成的发送工具
        senders.put(configurationId, sender);
        if (configurationId == null) {
            senders.put(DEFAULT_CONFIGURATION_KEY, sender);
        }

        return sender;
    }
}
