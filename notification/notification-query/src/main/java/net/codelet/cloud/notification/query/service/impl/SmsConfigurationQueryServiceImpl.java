package net.codelet.cloud.notification.query.service.impl;

import net.codelet.cloud.notification.query.dto.SmsConfigurationQueryDTO;
import net.codelet.cloud.notification.query.entity.SmsConfigurationQueryEntity;
import net.codelet.cloud.notification.query.repository.SmsConfigurationQueryRepository;
import net.codelet.cloud.notification.query.service.SmsConfigurationQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * 短信配置服务。
 */
@Component
public class SmsConfigurationQueryServiceImpl implements SmsConfigurationQueryService {

    private final SmsConfigurationQueryRepository smsConfigurationQueryRepository;

    @Autowired
    public SmsConfigurationQueryServiceImpl(
        SmsConfigurationQueryRepository smsConfigurationQueryRepository
    ) {
        this.smsConfigurationQueryRepository = smsConfigurationQueryRepository;
    }

    /**
     * 查询短信配置。
     * @param queryDTO 查询条件
     */
    @Override
    public Page<SmsConfigurationQueryEntity> search(SmsConfigurationQueryDTO queryDTO) {
        return smsConfigurationQueryRepository.findAllByCriteria(queryDTO, queryDTO.pageable());
    }

    /**
     * 取得默认的短信配置详细信息。
     */
    @Override
    public SmsConfigurationQueryEntity getDefault() {
        return smsConfigurationQueryRepository.findByIsDefaultIsTrueAndDeletedIsFalse().orElse(null);
    }

    /**
     * 取得短信配置详细信息。
     * @param configurationId 配置 ID
     */
    @Override
    public SmsConfigurationQueryEntity get(String configurationId) {
        return smsConfigurationQueryRepository.findByIdAndDeletedIsFalse(configurationId).orElse(null);
    }
}
