package net.codelet.cloud.auth.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.entity.BaseEntity;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class AccessTokenBaseEntity extends BaseEntity {

    private static final long serialVersionUID = 23101608569925L;

    @Getter
    @Setter
    @ApiModelProperty("所有者用户 ID")
    private String userId;

    @Getter
    @Setter
    @ApiModelProperty("应用 ID")
    private String appId;

    @Getter
    @Setter
    @ApiModelProperty("作用域")
    private String scope;

    @Getter
    @Setter
    @ApiModelProperty("最后更新时客户端 IP 地址")
    private String remoteAddr;

    @Getter
    @Setter
    @ApiModelProperty("客户端信息")
    private String client;

    @Getter
    @Setter
    @ApiModelProperty("客户端用户代理字符串")
    private String userAgent;

    @Getter
    @Setter
    @ApiModelProperty("用户信息更新版本号")
    private Long userRevision;

    @Getter
    @Setter
    @ApiModelProperty("创建时间")
    private Date createdAt;

    @Getter
    @Setter
    @ApiModelProperty("创建者 ID")
    private String createdBy;

    @Getter
    @Setter
    @ApiModelProperty("刷新时间")
    private Date refreshedAt;

    @Getter
    @Setter
    @ApiModelProperty("刷新者 ID")
    private String refreshedBy;

    @Getter
    @Setter
    @ApiModelProperty("有效截止时间")
    private Long expiresAt;

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
