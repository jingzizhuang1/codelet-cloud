package net.codelet.cloud.user.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.ReferenceEntity;
import net.codelet.cloud.entity.BaseVersionedEntity;
import net.codelet.cloud.vo.UserType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class UserBaseEntity extends BaseVersionedEntity {

    private static final long serialVersionUID = 115028391660655L;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("用户账号类型")
    private UserType type;

    @Getter
    @Setter
    @ApiModelProperty("姓名")
    private String name;

    @Getter
    @Setter
    @ApiModelProperty("姓名拼音")
    private String namePinyin;

    @Getter
    @Setter
    @ApiModelProperty("是否已停用")
    private Boolean disabled = false;

    @Getter
    @Setter
    @ApiModelProperty("停用时间")
    private Date disabledAt;

    @Getter
    @Setter
    @ReferenceEntity("user")
    @ApiModelProperty("停用者 ID")
    private String disabledBy;
}
