package net.codelet.cloud.notification.command.logic.impl;

import net.codelet.cloud.notification.command.entity.NotificationTemplateContentAvailableCommandEntity;
import net.codelet.cloud.notification.command.entity.NotificationTemplateLanguageCodesCommandEntity;
import net.codelet.cloud.notification.command.logic.NotificationTemplateCommandLogic;
import net.codelet.cloud.notification.command.repository.NotificationTemplateContentAvailableCommandRepository;
import net.codelet.cloud.notification.command.repository.NotificationTemplateContentLanguageCodesCommandRepository;
import net.codelet.cloud.notification.dto.NotificationTemplateDTO;
import net.codelet.cloud.service.StringRedisService;
import net.codelet.cloud.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 消息模版业务逻辑。
 */
@Component
public class NotificationTemplateCommandLogicImpl extends StringRedisService implements NotificationTemplateCommandLogic {

    private static final int LANGUAGE_REDIS_KEY_TTL = 300;
    private static final int CONTENT_REDIS_KEY_TTL = 30;

    private final NotificationTemplateContentLanguageCodesCommandRepository templateLanguageCodesRepository;
    private final NotificationTemplateContentAvailableCommandRepository contentRepository;

    @Autowired
    public NotificationTemplateCommandLogicImpl(
        StringRedisTemplate stringRedisTemplate,
        NotificationTemplateContentLanguageCodesCommandRepository templateLanguageCodesRepository,
        NotificationTemplateContentAvailableCommandRepository contentRepository
    ) {
        super(stringRedisTemplate);
        this.templateLanguageCodesRepository = templateLanguageCodesRepository;
        this.contentRepository = contentRepository;
    }

    /**
     * 模版支持的语言列表在 Redis 中的 KEY。
     * @param templateId 模版 ID
     * @return Redis KEY
     */
    private static String languageRedisKey(String templateId) {
        return "notification-templates:" + templateId + ":languages";
    }

    /**
     * 取得模版支持的语言列表。
     * @param templateId 模版 ID
     * @return 支持的语言列表
     */
    @Override
    public List<String> getSupportedLanguages(String templateId) {
        // 尝试从缓存中取得模版支持的语言列表
        final String languageRedisKey = languageRedisKey(templateId);
        String supportedLanguages = redisGet(languageRedisKey, LANGUAGE_REDIS_KEY_TTL);

        // 若缓存中不存在模版支持的语言列表则尝试从数据库中获取
        if (StringUtils.isEmpty(supportedLanguages)) {
            NotificationTemplateLanguageCodesCommandEntity templateEntity
                = templateLanguageCodesRepository.findById(templateId).orElse(null);
            if (templateEntity != null && templateEntity.getLanguageCodeList().size() > 0) {
                redisSet(languageRedisKey, templateEntity.getLanguageCodes(), LANGUAGE_REDIS_KEY_TTL);
                return templateEntity.getLanguageCodeList();
            }
            return null;
        // 否则将缓存中的值转为列表并返回
        } else {
            return new ArrayList<>(Arrays.asList(supportedLanguages.split(",")));
        }
    }

    /**
     * 模版本地化消息模版在 Redis 中的 KEY。
     * @param templateId   模版 ID
     * @param languageCode 语言代码
     * @return Redis KEY
     */
    private static String contentRedisKey(String templateId, String languageCode) {
        return "notification-templates:" + templateId + ":" + languageCode;
    }

    /**
     * 取得消息模版。
     * @param templateId          模版 ID
     * @param acceptLanguageCodes 接受的语言代码列表
     * @return 本地化消息模版
     */
    @Override
    public NotificationTemplateDTO get(String templateId, List<String> acceptLanguageCodes) {
        // 取得模版支持的语言列表，若无该模版则结束
        List<String> supportedLanguageCodes = getSupportedLanguages(templateId);
        if (supportedLanguageCodes == null) {
            return null;
        }

        // 若支持的语言中不包含任何接受的语言则结束
        supportedLanguageCodes.retainAll(acceptLanguageCodes);
        if (supportedLanguageCodes.size() == 0) {
            return null;
        }

        // 取得最优先的语言代码并尝试从缓存中取得本地化消息模版
        supportedLanguageCodes.sort(Comparator.comparingInt(acceptLanguageCodes::indexOf));
        String languageCode = supportedLanguageCodes.get(0);
        final String contentRedisKey = contentRedisKey(templateId, languageCode);
        NotificationTemplateDTO content
            = redisGetObject(contentRedisKey, NotificationTemplateDTO.class, CONTENT_REDIS_KEY_TTL);

        // 若缓存中存在本地化消息模版则将其返回
        if (content != null) {
            return content;
        }

        // 若缓存中不存在本地化消息模版则从数据库中获取
        NotificationTemplateContentAvailableCommandEntity contentEntity = contentRepository
            .findByTemplateIdAndLanguageCode(templateId, languageCode)
            .orElse(null);

        if (contentEntity == null) {
            return null;
        }

        // 缓存本地化消息模版并将其返回
        content = new NotificationTemplateDTO(contentEntity.getConfigurationId(), contentEntity);

        redisSetObject(contentRedisKey, content, CONTENT_REDIS_KEY_TTL);
        return content;
    }
}
