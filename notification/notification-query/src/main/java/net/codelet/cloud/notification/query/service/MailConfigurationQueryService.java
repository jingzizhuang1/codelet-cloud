package net.codelet.cloud.notification.query.service;

import net.codelet.cloud.notification.query.dto.MailConfigurationQueryDTO;
import net.codelet.cloud.notification.query.entity.MailConfigurationQueryEntity;
import org.springframework.data.domain.Page;

/**
 * 电子邮件配置服务接口。
 */
public interface MailConfigurationQueryService {

    /**
     * 查询电子邮件配置。
     * @param queryDTO 查询条件
     */
    Page<MailConfigurationQueryEntity> search(MailConfigurationQueryDTO queryDTO);

    /**
     * 取得默认的电子邮件配置详细信息。
     */
    MailConfigurationQueryEntity getDefault();

    /**
     * 取得电子邮件配置详细信息。
     * @param configurationId 配置 ID
     */
    MailConfigurationQueryEntity get(String configurationId);
}
