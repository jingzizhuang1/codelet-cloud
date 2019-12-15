package net.codelet.cloud.notification.query.service.impl;

import net.codelet.cloud.notification.query.dto.MailConfigurationQueryDTO;
import net.codelet.cloud.notification.query.entity.MailConfigurationQueryEntity;
import net.codelet.cloud.notification.query.repository.MailConfigurationQueryRepository;
import net.codelet.cloud.notification.query.service.MailConfigurationQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * 电子邮件配置服务。
 */
@Component
public class MailConfigurationQueryServiceImpl implements MailConfigurationQueryService {

    private final MailConfigurationQueryRepository mailRepository;

    @Autowired
    public MailConfigurationQueryServiceImpl(
        MailConfigurationQueryRepository mailRepository
    ) {
        this.mailRepository = mailRepository;
    }

    /**
     * 查询电子邮件配置。
     * @param queryDTO 查询条件
     */
    @Override
    public Page<MailConfigurationQueryEntity> search(MailConfigurationQueryDTO queryDTO) {
        return mailRepository.findAllByCriteria(queryDTO, queryDTO.pageable());
    }

    /**
     * 取得默认的电子邮件配置详细信息。
     */
    @Override
    public MailConfigurationQueryEntity getDefault() {
        return mailRepository.findByIsDefaultIsTrueAndDeletedIsFalse().orElse(null);
    }

    /**
     * 取得电子邮件配置详细信息。
     * @param configurationId 配置 ID
     */
    @Override
    public MailConfigurationQueryEntity get(String configurationId) {
        return mailRepository.findByIdAndDeletedIsFalse(configurationId).orElse(null);
    }
}
