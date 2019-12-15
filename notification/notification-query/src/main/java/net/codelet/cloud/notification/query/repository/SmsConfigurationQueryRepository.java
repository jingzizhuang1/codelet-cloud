package net.codelet.cloud.notification.query.repository;

import net.codelet.cloud.notification.query.entity.SmsConfigurationQueryEntity;
import net.codelet.cloud.repository.BaseRepository;

import java.util.Optional;

/**
 * 短信发送配置数据仓库。
 */
public interface SmsConfigurationQueryRepository extends BaseRepository<SmsConfigurationQueryEntity> {

    /**
     * 取得默认配置。
     * @return 短信发送配置
     */
    Optional<SmsConfigurationQueryEntity> findByIsDefaultIsTrueAndDeletedIsFalse();

    /**
     * 取得指定的默认配置。
     * @return 短信发送配置
     */
    Optional<SmsConfigurationQueryEntity> findByIdAndDeletedIsFalse(String configurationId);
}
