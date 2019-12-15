package net.codelet.cloud.notification.query.entity;

import net.codelet.cloud.notification.entity.NotificationBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "notification")
public class NotificationQueryEntity extends NotificationBaseEntity {

    private static final long serialVersionUID = 119037419961684L;

}
