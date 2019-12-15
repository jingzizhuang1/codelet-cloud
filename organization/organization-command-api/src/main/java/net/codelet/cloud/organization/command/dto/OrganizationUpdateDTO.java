package net.codelet.cloud.organization.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;

import javax.validation.constraints.NotBlank;

/**
 * 组织信息更新数据传输对象。
 */
public class OrganizationUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = -8921295436950958453L;

    @Getter
    @Setter
    @NotBlank
    @ApiModelProperty("组织名称")
    private String name;

    @Getter
    @Setter
    @ApiModelProperty("组织描述")
    private String description;
}
