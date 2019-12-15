package net.codelet.cloud.notification.command.entity;

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
public class SmsConfigurationCommandEntity extends SmsConfigurationBaseEntity {

    private static final long serialVersionUID = -6365247333369867681L;

    @Getter
    @Setter
    @ApiModelProperty("账号密码或访问密钥")
    private String password;
}
