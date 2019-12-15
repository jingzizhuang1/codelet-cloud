package net.codelet.cloud.notification.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.entity.BaseVersionedEntity;
import net.codelet.cloud.notification.vo.SmsProvider;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 短信发送配置数据实体。
 */
@MappedSuperclass
public class SmsConfigurationBaseEntity extends BaseVersionedEntity {

    private static final long serialVersionUID = -4893846732833386384L;

    @Getter
    @Setter
    @ApiModelProperty("配置名称")
    private String name;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("短信服务供应商")
    private SmsProvider provider;

    @Getter
    @Setter
    @ApiModelProperty("账号名或访问 KEY")
    private String username;

    @Getter
    @Setter
    @ApiModelProperty("账号密码或访问密钥")
    private String password;

    @Getter
    @Setter
    @ApiModelProperty("短信签名")
    private String signName;

    @Getter
    @Setter
    @ApiModelProperty("是否为默认配置")
    private Boolean isDefault;

    @Getter
    @Setter
    @ApiModelProperty("是否已被停用")
    private Boolean disabled = false;

    @Getter
    @Setter
    @ApiModelProperty("停用时间")
    private Date disabledAt;

    @Getter
    @Setter
    @ApiModelProperty("停用操作者用户 ID")
    private String disabledBy;
}
