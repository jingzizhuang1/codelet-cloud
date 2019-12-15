package net.codelet.cloud.auth.command.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "credential_password")
public class CredentialPasswordEntity extends BaseEntity {

    private static final long serialVersionUID = -1350237009131844074L;

    @Getter
    @Setter
    private String userId;

    @Getter
    @Setter
    private String credential;

    @Getter
    @Setter
    private String password;
}
