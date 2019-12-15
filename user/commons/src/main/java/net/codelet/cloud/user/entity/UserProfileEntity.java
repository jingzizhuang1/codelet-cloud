package net.codelet.cloud.user.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.vo.Gender;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class UserProfileEntity extends UserBaseEntity {

    private static final long serialVersionUID = 601833190685802846L;

    @Getter
    @Setter
    @ApiModelProperty("手机号码")
    private String mobile;

    @Getter
    @Setter
    @ApiModelProperty("电子邮箱地址")
    private String email;

    @Getter
    @Setter
    @ApiModelProperty("出生日期")
    private Date birthDate;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("性别")
    private Gender gender;
}
