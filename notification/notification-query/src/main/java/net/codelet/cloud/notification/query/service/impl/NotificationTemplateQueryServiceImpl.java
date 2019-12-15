package net.codelet.cloud.notification.query.service.impl;

import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.notification.query.dto.NotificationTemplateGetDTO;
import net.codelet.cloud.notification.query.dto.NotificationTemplateQueryDTO;
import net.codelet.cloud.notification.query.entity.NotificationTemplateQueryEntity;
import net.codelet.cloud.notification.query.entity.NotificationTemplateWithContentsQueryEntity;
import net.codelet.cloud.notification.query.repository.NotificationTemplateQueryRepository;
import net.codelet.cloud.notification.query.repository.NotificationTemplateWithContentsQueryRepository;
import net.codelet.cloud.notification.query.service.NotificationTemplateQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * 通知消息模版服务。
 */
@Component
public class NotificationTemplateQueryServiceImpl implements NotificationTemplateQueryService {

    private final NotificationTemplateQueryRepository templateRepository;
    private final NotificationTemplateWithContentsQueryRepository templateWithContentsRepository;

    @Autowired
    public NotificationTemplateQueryServiceImpl(
        NotificationTemplateQueryRepository templateRepository,
        NotificationTemplateWithContentsQueryRepository templateWithContentsRepository
    ) {
        this.templateRepository = templateRepository;
        this.templateWithContentsRepository = templateWithContentsRepository;
    }

    /**
     * 查询通知模版。
     * @param queryDTO 查询条件
     * @return 通知模版分页数据
     */
    @Override
    public Page<NotificationTemplateQueryEntity> search(NotificationTemplateQueryDTO queryDTO) {
        return templateRepository.findAllByCriteria(queryDTO, queryDTO.pageable());
    }

    /**
     * 取得通知消息模版。
     * @param templateId 模版 ID
     * @param queryDTO   查询过滤条件
     * @return 通知消息模版
     */
    @Override
    public NotificationTemplateWithContentsQueryEntity get(
        String templateId,
        NotificationTemplateGetDTO queryDTO
    ) {
        queryDTO.setId(templateId);
        return templateWithContentsRepository
            .findOneByCriteria(queryDTO, queryDTO.getRevision()).orElse(null);
    }

    /**
     * 检查模版是否存在。
     * @param templateId 模版 ID
     * @param disabled   是否已被停用
     * @return 检查结果
     */
    @Override
    public boolean exists(String templateId, Boolean disabled) {
        try {
            NotificationTemplateGetDTO queryDTO = new NotificationTemplateGetDTO();
            queryDTO.setId(templateId);
            queryDTO.setDisabled(disabled);
            return templateWithContentsRepository
                .findOneByCriteria(queryDTO, queryDTO.getRevision())
                .isPresent();
        } catch (NotFoundError e) {
            return false;
        }
    }
}
