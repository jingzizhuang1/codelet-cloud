package net.codelet.cloud.notification.query.api;

import net.codelet.cloud.notification.query.dto.SmsConfigurationQueryDTO;
import net.codelet.cloud.notification.query.entity.SmsConfigurationQueryEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "${services.notification.query.name:notification-query}",
    contextId = "sms-configuration-query"
)
public interface SmsConfigurationQueryApi {

    /**
     * 查询短信发送配置。
     * @param queryDTO 查询条件
     */
    @GetMapping("/sms-configurations")
    Page<SmsConfigurationQueryEntity> search(SmsConfigurationQueryDTO queryDTO);

    /**
     * 取得默认的短信发送配置详细信息。
     */
    @GetMapping("/sms-configurations/default")
    SmsConfigurationQueryEntity getDefault();

    /**
     * 取得短信发送配置详细信息。
     * @param configurationId 短信发送配置信息
     */
    @GetMapping("/sms-configurations/{configurationId}")
    SmsConfigurationQueryEntity get(@PathVariable("configurationId") String configurationId);
}
