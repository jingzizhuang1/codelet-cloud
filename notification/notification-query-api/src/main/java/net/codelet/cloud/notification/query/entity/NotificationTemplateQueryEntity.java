package net.codelet.cloud.notification.query.entity;

import net.codelet.cloud.notification.entity.NotificationTemplateBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "notification_template")
public class NotificationTemplateQueryEntity extends NotificationTemplateBaseEntity {

    private static final long serialVersionUID = -3980556436832625230L;
}
