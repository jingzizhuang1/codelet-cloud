package net.codelet.cloud.notification.query.entity;

import net.codelet.cloud.notification.entity.MailConfigurationBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 电子邮件发送配置数据实体。
 */
@Entity
@Table(name = "mail_configuration")
public class MailConfigurationQueryEntity extends MailConfigurationBaseEntity {
    private static final long serialVersionUID = -7334150514496321742L;
}
