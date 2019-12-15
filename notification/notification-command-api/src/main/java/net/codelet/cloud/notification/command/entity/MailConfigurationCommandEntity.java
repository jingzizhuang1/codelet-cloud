package net.codelet.cloud.notification.command.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.notification.entity.MailConfigurationBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 电子邮件发送配置数据实体。
 */
@Entity
@Table(name = "mail_configuration")
public class MailConfigurationCommandEntity extends MailConfigurationBaseEntity {

    private static final long serialVersionUID = -8022229646932098568L;

    @Getter
    @Setter
    @ApiModelProperty("密码")
    private String password;
}
