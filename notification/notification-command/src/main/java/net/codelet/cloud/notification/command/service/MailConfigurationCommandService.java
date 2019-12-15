package net.codelet.cloud.notification.command.service;

import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.notification.command.dto.MailConfigurationCreateDTO;
import net.codelet.cloud.notification.command.dto.MailConfigurationUpdateDTO;
import net.codelet.cloud.notification.command.entity.MailConfigurationCommandEntity;

/**
 * 电子邮件配置服务接口。
 */
public interface MailConfigurationCommandService {

    /**
     * 创建电子邮件配置。
     * @param operator  操作者信息
     * @param createDTO 电子邮件配置信息
     */
    MailConfigurationCommandEntity create(OperatorDTO operator, MailConfigurationCreateDTO createDTO);

    /**
     * 更新电子邮件配置。
     * @param operator        操作者信息
     * @param configurationId 配置 ID
     * @param updateDTO       电子邮件配置信息
     * @param revision        修订版本号
     */
    void update(OperatorDTO operator, String configurationId, MailConfigurationUpdateDTO updateDTO, Long revision);

    /**
     * 将配置设置为默认配置。
     * @param operator        操作者信息
     * @param configurationId 配置 ID
     * @param revision        修订版本号
     */
    void setAsDefault(OperatorDTO operator, String configurationId, Long revision);

    /**
     * 设置配置的停用状态。
     * @param operator        操作者信息
     * @param configurationId 配置 ID
     * @param revision        修订版本号
     */
    void setDisabled(OperatorDTO operator, String configurationId, Long revision, Boolean disabled);

    /**
     * 删除配置。
     * @param operator        操作者信息
     * @param configurationId 配置 ID
     * @param revision        修订版本号
     */
    void delete(OperatorDTO operator, String configurationId, Long revision);
}
