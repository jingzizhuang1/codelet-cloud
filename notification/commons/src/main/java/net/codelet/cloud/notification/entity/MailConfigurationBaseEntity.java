package net.codelet.cloud.notification.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.entity.BaseVersionedEntity;

import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 电子邮件发送配置数据实体。
 */
@MappedSuperclass
public class MailConfigurationBaseEntity extends BaseVersionedEntity {

    private static final long serialVersionUID = -2665794385279335046L;

    @Getter
    @Setter
    @ApiModelProperty("配置名称")
    private String name;

    @Getter
    @Setter
    @ApiModelProperty("主机名")
    private String host;

    @Getter
    @Setter
    @ApiModelProperty("端口号")
    private Integer port;

    @Getter
    @Setter
    @ApiModelProperty("协议")
    private String protocol;

    @Getter
    @Setter
    @ApiModelProperty("发送者名称")
    private String senderName;

    @Getter
    @Setter
    @ApiModelProperty("账号")
    private String username;

    @Getter
    @Setter
    @ApiModelProperty("是否启用 StartTLS")
    private Boolean startTlsEnabled;

    @Getter
    @Setter
    @ApiModelProperty("是否必须使用 StartTLS")
    private Boolean startTlsRequired;

    @Getter
    @Setter
    @ApiModelProperty("连接超时时长（秒）")
    private Integer connectionTimeout;

    @Getter
    @Setter
    @ApiModelProperty("读取超时时长（秒）")
    private Integer readTimeout;

    @Getter
    @Setter
    @ApiModelProperty("写入超时时长（秒）")
    private Integer writeTimeout;

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

    @JsonIgnore
    public Integer getConnectionTimeoutMilliseconds() {
        return connectionTimeout * 1000;
    }

    @JsonIgnore
    public Integer getReadTimeoutMilliseconds() {
        return readTimeout * 1000;
    }

    @JsonIgnore
    public Integer getWriteTimeoutMilliseconds() {
        return writeTimeout * 1000;
    }
}
