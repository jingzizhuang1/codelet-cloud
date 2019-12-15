package net.codelet.cloud.notification.command.service;

import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.notification.command.dto.SmsConfigurationCreateDTO;
import net.codelet.cloud.notification.command.dto.SmsConfigurationUpdateDTO;
import net.codelet.cloud.notification.command.entity.SmsConfigurationCommandEntity;

/**
 * 短信配置服务接口。
 */
public interface SmsConfigurationCommandService {

    /**
     * 创建短信配置。
     * @param operator  操作者信息
     * @param createDTO 短信配置信息
     */
    SmsConfigurationCommandEntity create(OperatorDTO operator, SmsConfigurationCreateDTO createDTO);

    /**
     * 更新短信配置。
     * @param operator        操作者信息
     * @param configurationId 配置 ID
     * @param updateDTO       短信配置信息
     * @param revision        修订版本号
     */
    void update(OperatorDTO operator, String configurationId, SmsConfigurationUpdateDTO updateDTO, Long revision);

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
