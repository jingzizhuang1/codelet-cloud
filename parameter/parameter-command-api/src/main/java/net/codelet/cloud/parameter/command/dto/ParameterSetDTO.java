package net.codelet.cloud.parameter.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 参数设置数据传输对象。
 */
public class ParameterSetDTO extends BaseDTO {

    private static final long serialVersionUID = 595568132914028296L;

    @Getter
    @Setter
    @NotNull
    @Size(max = 255)
    @ApiModelProperty("参数值")
    private String value;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 255)
    @ApiModelProperty("参数说明")
    private String description;
}
