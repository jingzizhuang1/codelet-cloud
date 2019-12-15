package net.codelet.cloud.notification.command.entity;

import net.codelet.cloud.notification.entity.NotificationTemplateBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 通知消息模版数据实体。
 */
@Entity
@Table(name = "notification_template")
public class NotificationTemplateCommandEntity extends NotificationTemplateBaseEntity {

    private static final long serialVersionUID = -8754488031409818110L;
}
