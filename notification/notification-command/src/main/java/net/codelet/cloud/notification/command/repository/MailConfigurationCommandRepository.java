package net.codelet.cloud.notification.command.repository;

import net.codelet.cloud.notification.command.entity.MailConfigurationCommandEntity;
import net.codelet.cloud.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * 电子邮件发送配置数据仓库。
 */
public interface MailConfigurationCommandRepository extends BaseRepository<MailConfigurationCommandEntity> {

    /**
     * 取得默认配置。
     * @return 电子邮件发送配置
     */
    Optional<MailConfigurationCommandEntity> findByIsDefaultIsTrueAndDeletedIsFalse();

    /**
     * 根据 ID 取得配置信息。
     * @param id 配置 ID
     * @return 配置信息
     */
    Optional<MailConfigurationCommandEntity> findByIdAndDeletedIsFalse(String id);

    /**
     * 取得配置信息件数。
     * @return 件数
     */
    Integer countByDeletedIsFalse();

    /**
     * 将所有（正常情况下仅一件）默认配置更新为非默认。
     */
    @Query("UPDATE MailConfigurationCommandEntity c SET c.isDefault = false WHERE c.isDefault = true")
    void unsetDefaults();
}
