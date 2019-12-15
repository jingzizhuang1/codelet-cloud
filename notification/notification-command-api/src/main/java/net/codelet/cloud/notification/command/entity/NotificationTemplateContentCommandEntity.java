package net.codelet.cloud.notification.command.entity;

import net.codelet.cloud.notification.entity.NotificationTemplateContentBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 通知消息模版本地化内容数据实体。
 */
@Entity
@Table(name = "notification_template_content")
public class NotificationTemplateContentCommandEntity extends NotificationTemplateContentBaseEntity {
    private static final long serialVersionUID = -688017020902642608L;
}
