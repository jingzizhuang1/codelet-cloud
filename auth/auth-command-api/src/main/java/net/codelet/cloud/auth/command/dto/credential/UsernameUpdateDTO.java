package net.codelet.cloud.auth.command.dto.credential;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.util.RegExpUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UsernameUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = -7578045540828162337L;

    @Getter
    @Setter
    @NotEmpty
    @Pattern(regexp = RegExpUtils.USERNAME)
    @ApiModelProperty("登录用户名")
    private String username;
}
