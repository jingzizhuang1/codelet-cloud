package net.codelet.cloud.notification.command.repository;

import net.codelet.cloud.notification.command.entity.SmsConfigurationCommandEntity;
import net.codelet.cloud.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * 短信发送配置数据仓库。
 */
public interface SmsConfigurationCommandRepository extends BaseRepository<SmsConfigurationCommandEntity> {

    /**
     * 取得默认配置。
     * @return 短信发送配置
     */
    Optional<SmsConfigurationCommandEntity> findByIsDefaultIsTrueAndDeletedIsFalse();

    /**
     * 取得指定的配置。
     * @param id 短信发送配置 ID
     * @return 短信发送配置
     */
    Optional<SmsConfigurationCommandEntity> findByIdAndDeletedIsFalse(String id);

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
