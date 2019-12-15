package net.codelet.cloud.notification.query.entity;

import net.codelet.cloud.notification.entity.NotificationTemplateContentBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "notification_template_content")
public class NotificationTemplateContentQueryEntity extends NotificationTemplateContentBaseEntity {
    private static final long serialVersionUID = -8735927457222434234L;
}
