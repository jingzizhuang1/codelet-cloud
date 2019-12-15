package net.codelet.cloud.notification.query.repository;

import net.codelet.cloud.notification.query.entity.MailConfigurationQueryEntity;
import net.codelet.cloud.repository.BaseRepository;

import java.util.Optional;

/**
 * 电子邮件发送配置数据仓库。
 */
public interface MailConfigurationQueryRepository extends BaseRepository<MailConfigurationQueryEntity> {

    /**
     * 取得默认配置。
     * @return 电子邮件发送配置
     */
    Optional<MailConfigurationQueryEntity> findByIsDefaultIsTrueAndDeletedIsFalse();

    /**
     * 取得指定的默认配置。
     * @return 电子邮件发送配置
     */
    Optional<MailConfigurationQueryEntity> findByIdAndDeletedIsFalse(String configurationId);
}
