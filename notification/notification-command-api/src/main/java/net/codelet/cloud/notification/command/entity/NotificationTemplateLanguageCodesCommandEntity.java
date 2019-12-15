package net.codelet.cloud.notification.command.entity;

import net.codelet.cloud.notification.entity.NotificationTemplateLanguageCodesBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 通知消息模版可用语言代码信息数据实体。
 */
@Entity
@Table(name = "notification_template_language_codes")
public class NotificationTemplateLanguageCodesCommandEntity extends NotificationTemplateLanguageCodesBaseEntity {
    private static final long serialVersionUID = 5124005248907919831L;
}
