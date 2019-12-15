package net.codelet.cloud.notification.command.api;

import net.codelet.cloud.notification.command.dto.MailConfigurationCreateDTO;
import net.codelet.cloud.notification.command.dto.MailConfigurationUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "${services.notification.command.name:notification-command}",
    contextId = "mail-configuration-command"
)
public interface MailConfigurationCommandApi {

    /**
     * 创建电子邮件发送配置。
     * @param createDTO 电子邮件发送配置信息
     */
    @PostMapping("/mail-configurations")
    void create(@RequestBody MailConfigurationCreateDTO createDTO);

    /**
     * 更新电子邮件发送配置。
     * @param updateDTO 电子邮件发送配置信息
     */
    @PatchMapping("/mail-configurations/{configurationId}")
    void update(
        @PathVariable("configurationId") String configurationId,
        @RequestParam("revision") long revision,
        @RequestBody MailConfigurationUpdateDTO updateDTO
    );

    /**
     * 将电子邮件发送配置设置为默认配置。
     */
    @PostMapping("/mail-configurations/{configurationId}/set-as-default")
    void setAsDefault(
        @PathVariable("configurationId") String configurationId,
        @RequestParam("revision") long revision
    );

    /**
     * 停用电子邮件发送配置。
     */
    @PostMapping("/mail-configurations/{configurationId}/disable")
    void disable(
        @PathVariable("configurationId") String configurationId,
        @RequestParam("revision") long revision
    );

    /**
     * 启用电子邮件发送配置。
     */
    @PostMapping("/mail-configurations/{configurationId}/enable")
    void enable(
        @PathVariable("configurationId") String configurationId,
        @RequestParam("revision") long revision
    );

    /**
     * 删除电子邮件发送配置。
     */
    @DeleteMapping("/mail-configurations/{configurationId}")
    void delete(
        @PathVariable("configurationId") String configurationId,
        @RequestParam("revision") long revision
    );
}
