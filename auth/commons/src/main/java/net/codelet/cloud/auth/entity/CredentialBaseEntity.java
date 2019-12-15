package net.codelet.cloud.auth.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.auth.vo.CredentialType;
import net.codelet.cloud.entity.BaseVersionedEntity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class CredentialBaseEntity extends BaseVersionedEntity {

    private static final long serialVersionUID = 161584224108505L;

    @Getter
    @Setter
    @ApiModelProperty("所有者用户 ID")
    private String userId;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("认证凭证类型")
    private CredentialType type;

    @Getter
    @Setter
    @ApiModelProperty("认证凭证")
    private String credential;
}
