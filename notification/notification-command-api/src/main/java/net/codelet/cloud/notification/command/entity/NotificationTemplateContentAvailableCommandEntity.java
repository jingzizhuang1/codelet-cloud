package net.codelet.cloud.notification.command.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.notification.entity.NotificationTemplateContentBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 通知消息模版本地化内容数据实体。
 */
@Entity
@Table(name = "notification_template_content_available")
public class NotificationTemplateContentAvailableCommandEntity extends NotificationTemplateContentBaseEntity {

    private static final long serialVersionUID = 7888025411124158972L;

    @Getter
    @Setter
    @ApiModelProperty("电子邮件或短信发送配置 ID，未指定时使用默认配置")
    private String configurationId;
}
