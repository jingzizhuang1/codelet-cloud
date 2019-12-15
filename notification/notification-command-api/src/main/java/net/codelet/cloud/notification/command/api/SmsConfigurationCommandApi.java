package net.codelet.cloud.notification.command.api;

import net.codelet.cloud.notification.command.dto.SmsConfigurationCreateDTO;
import net.codelet.cloud.notification.command.dto.SmsConfigurationUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "${services.notification.command.name:notification-command}",
    contextId = "sms-configuration-command"
)
public interface SmsConfigurationCommandApi {

    /**
     * 创建短信发送配置。
     * @param createDTO 短信发送配置信息
     */
    @PostMapping("/sms-configurations")
    void create(@RequestBody SmsConfigurationCreateDTO createDTO);

    /**
     * 更新短信发送配置。
     * @param updateDTO 短信发送配置信息
     */
    @PatchMapping("/sms-configurations/{configurationId}")
    void update(
        @PathVariable("configurationId") String configurationId,
        @RequestParam("revision") long revision,
        @RequestBody SmsConfigurationUpdateDTO updateDTO
    );

    /**
     * 将短信发送配置设置为默认配置。
     */
    @PostMapping("/sms-configurations/{configurationId}/set-as-default")
    void setAsDefault(
        @PathVariable("configurationId") String configurationId,
        @RequestParam("revision") long revision
    );

    /**
     * 停用短信发送配置。
     */
    @PostMapping("/sms-configurations/{configurationId}/disable")
    void disable(
        @PathVariable("configurationId") String configurationId,
        @RequestParam("revision") long revision
    );

    /**
     * 启用短信发送配置。
     */
    @PostMapping("/sms-configurations/{configurationId}/enable")
    void enable(
        @PathVariable("configurationId") String configurationId,
        @RequestParam("revision") long revision
    );

    /**
     * 删除短信发送配置。
     */
    @DeleteMapping("/sms-configurations/{configurationId}")
    void delete(
        @PathVariable("configurationId") String configurationId,
        @RequestParam("revision") long revision
    );
}
