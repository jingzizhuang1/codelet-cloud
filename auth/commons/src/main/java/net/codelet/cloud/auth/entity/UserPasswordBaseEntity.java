package net.codelet.cloud.auth.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.entity.BaseEntity;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class UserPasswordBaseEntity extends BaseEntity {

    private static final long serialVersionUID = 8649853868764112176L;

    @Getter
    @Setter
    @ApiModelProperty("所有者用户 ID")
    private String userId;

    @Getter
    @Setter
    @ApiModelProperty("登录密码")
    private String password;

    @Getter
    @Setter
    @ApiModelProperty("密码强度")
    private Integer strength;

    @Getter
    @Setter
    @ApiModelProperty("散列摘要（md5(password + userId)）")
    private String hash;

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
    @ApiModelProperty("是否已删除")
    private Boolean deleted = false;

    @Getter
    @Setter
    @ApiModelProperty("删除时间")
    private Date deletedAt;

    @Getter
    @Setter
    @ApiModelProperty("删除者 ID")
    private String deletedBy;
}
