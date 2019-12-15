package net.codelet.cloud.notification.query.api;

import net.codelet.cloud.notification.query.dto.MailConfigurationQueryDTO;
import net.codelet.cloud.notification.query.entity.MailConfigurationQueryEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "${services.notification.query.name:notification-query}",
    contextId = "mail-configuration-query"
)
public interface MailConfigurationQueryApi {

    /**
     * 查询电子邮件发送配置。
     * @param queryDTO 查询条件
     */
    @GetMapping("/mail-configurations")
    Page<MailConfigurationQueryEntity> search(MailConfigurationQueryDTO queryDTO);

    /**
     * 取得默认的电子邮件发送配置详细信息。
     */
    @GetMapping("/mail-configurations/default")
    MailConfigurationQueryEntity getDefault();

    /**
     * 取得电子邮件发送配置详细信息。
     * @param configurationId 电子邮件发送配置信息
     */
    @GetMapping("/mail-configurations/{configurationId}")
    MailConfigurationQueryEntity get(@PathVariable("configurationId") String configurationId);
}
