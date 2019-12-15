package net.codelet.cloud.notification.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.ReferenceEntity;
import net.codelet.cloud.entity.BaseEntity;
import net.codelet.cloud.vo.PrincipalType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 通知数据实体。
 */
@MappedSuperclass
public class NotificationBaseEntity extends BaseEntity {

    private static final long serialVersionUID = 265743922615743L;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("发送者类型")
    private PrincipalType senderType;

    @Getter
    @Setter
    @ApiModelProperty("发送者 ID")
    private String senderId;

    @Getter
    @Setter
    @ApiModelProperty("接受者用户 ID")
    private String receiverId;

    @Getter
    @Setter
    @ApiModelProperty("标题")
    private String title;

    @Getter
    @Setter
    @ApiModelProperty("内容类型")
    private String contentType;

    @Getter
    @Setter
    @ApiModelProperty("内容")
    private String content;

    @Getter
    @Setter
    @ApiModelProperty("相关实体类型")
    private String targetType;

    @Getter
    @Setter
    @ApiModelProperty("相关实体 ID")
    private String targetId;

    @Getter
    @Setter
    @ApiModelProperty("创建时间")
    private Date createdAt;

    @Getter
    @Setter
    @ReferenceEntity("user")
    @ApiModelProperty("创建者 ID")
    private String createdBy;

    @Getter
    @Setter
    @ApiModelProperty("阅读时间")
    private Date readAt;

    @Getter
    @Setter
    @ApiModelProperty("过期时间")
    private Date expiresAt;
}
