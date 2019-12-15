package net.codelet.cloud.notification.command.entity;

import net.codelet.cloud.notification.entity.NotificationBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 通知数据实体。
 */
@Entity
@Table(name = "notification")
public class NotificationCommandEntity extends NotificationBaseEntity {

    private static final long serialVersionUID = 155227940019292L;
}
