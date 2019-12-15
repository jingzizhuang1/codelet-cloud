package net.codelet.cloud.organization.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.organization.vo.DivisionType;

import javax.validation.constraints.NotNull;

/**
 * 事业部信息更新数据传输对象。
 */
public class DivisionUpdateDTO extends OrganizationUpdateDTO {

    private static final long serialVersionUID = 4055207068839427779L;

    @Getter
    @Setter
    @NotNull
    @ApiModelProperty("事业部类型")
    private DivisionType divisionType;
}
