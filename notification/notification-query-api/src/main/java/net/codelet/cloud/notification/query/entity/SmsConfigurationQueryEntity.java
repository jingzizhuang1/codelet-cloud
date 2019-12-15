package net.codelet.cloud.notification.query.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.notification.entity.SmsConfigurationBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 短信发送配置数据实体。
 */
@Entity
@Table(name = "sms_configuration")
public class SmsConfigurationQueryEntity extends SmsConfigurationBaseEntity {
    private static final long serialVersionUID = -5078566001721310638L;
}
