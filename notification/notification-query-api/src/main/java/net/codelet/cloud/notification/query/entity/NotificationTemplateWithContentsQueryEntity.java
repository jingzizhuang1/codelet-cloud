package net.codelet.cloud.notification.query.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.notification.entity.NotificationTemplateBaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "notification_template")
public class NotificationTemplateWithContentsQueryEntity extends NotificationTemplateBaseEntity {

    private static final long serialVersionUID = -810124896975950387L;

    @Getter
    @Setter
    @OneToMany(mappedBy = "templateId", cascade = {CascadeType.ALL})
    @OrderBy("languageCode")
    @ApiModelProperty("地区语言模版列表")
    private List<NotificationTemplateContentQueryEntity> contents;
}
