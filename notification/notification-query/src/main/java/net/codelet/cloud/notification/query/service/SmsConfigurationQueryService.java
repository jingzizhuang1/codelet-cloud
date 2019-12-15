package net.codelet.cloud.notification.query.service;

import net.codelet.cloud.notification.query.dto.SmsConfigurationQueryDTO;
import net.codelet.cloud.notification.query.entity.SmsConfigurationQueryEntity;
import org.springframework.data.domain.Page;

/**
 * 短信配置服务接口。
 */
public interface SmsConfigurationQueryService {

    /**
     * 查询短信配置。
     * @param queryDTO 查询条件
     */
    Page<SmsConfigurationQueryEntity> search(SmsConfigurationQueryDTO queryDTO);

    /**
     * 取得默认的短信配置详细信息。
     */
    SmsConfigurationQueryEntity getDefault();

    /**
     * 取得短信配置详细信息。
     * @param configurationId 配置 ID
     */
    SmsConfigurationQueryEntity get(String configurationId);
}
