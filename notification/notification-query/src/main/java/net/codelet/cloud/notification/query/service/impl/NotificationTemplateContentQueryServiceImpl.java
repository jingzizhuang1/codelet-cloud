package net.codelet.cloud.notification.query.service.impl;

import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.notification.query.dto.NotificationTemplateContentQueryDTO;
import net.codelet.cloud.notification.query.entity.NotificationTemplateContentQueryEntity;
import net.codelet.cloud.notification.query.repository.NotificationTemplateContentQueryRepository;
import net.codelet.cloud.notification.query.service.NotificationTemplateContentQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * 通知消息模版服务。
 */
@Component
public class NotificationTemplateContentQueryServiceImpl implements NotificationTemplateContentQueryService {

    private final NotificationTemplateContentQueryRepository contentRepository;

    @Autowired
    public NotificationTemplateContentQueryServiceImpl(
        NotificationTemplateContentQueryRepository contentRepository
    ) {
        this.contentRepository = contentRepository;
    }

    /**
     * 查询通知模版。
     * @param templateId 模版 ID
     * @param queryDTO   查询条件
     * @return 通知模版分页数据
     */
    @Override
    public Page<NotificationTemplateContentQueryEntity> search(
        String templateId,
        NotificationTemplateContentQueryDTO queryDTO
    ) {
        queryDTO.setTemplateId(templateId);
        return contentRepository.findAllByCriteria(queryDTO, queryDTO.pageable());
    }

    /**
     * 取得通知消息模版。
     * @param templateId   模版 ID
     * @param languageCode 语言代码
     * @return 通知消息模版
     */
    @Override
    public NotificationTemplateContentQueryEntity get(String templateId, String languageCode) {
        // 根据模版 ID 及语言代码取得模版内容
        NotificationTemplateContentQueryEntity contentEntity = contentRepository
            .findByTemplateIdAndLanguageCodeAndDeletedIsFalse(templateId, languageCode)
            .orElse(null);

        // 若未能取得语言的地区模版内容，则尝试取得语言的通用模版内容
        if (contentEntity == null && languageCode.contains("_")) {
            contentEntity = contentRepository
                .findByTemplateIdAndLanguageCodeAndDeletedIsFalse(templateId, languageCode.split("_")[0])
                .orElse(null);
        }

        if (contentEntity == null) {
            throw new NotFoundError();
        }
        return contentEntity;
    }
}
