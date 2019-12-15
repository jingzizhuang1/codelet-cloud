package net.codelet.cloud.parameter.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.entity.BaseVersionedEntity;

import javax.persistence.MappedSuperclass;

/**
 * 全局参数。
 */
@MappedSuperclass
public class ParameterBaseEntity extends BaseVersionedEntity {

    private static final long serialVersionUID = 35968990056753L;

    @Getter
    @Setter
    @ApiModelProperty("应用 ID")
    private String appId;

    @Getter
    @Setter
    @ApiModelProperty("参数名")
    private String name;

    @Getter
    @Setter
    @ApiModelProperty("参数值")
    private String value;

    @Getter
    @Setter
    @ApiModelProperty("参数描述")
    private String description;
}
