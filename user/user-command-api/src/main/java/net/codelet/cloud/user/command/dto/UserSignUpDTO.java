package net.codelet.cloud.user.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.util.RegExpUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserSignUpDTO extends UserProfileUpdateDTO {

    private static final long serialVersionUID = 6865990774189077282L;

    @Getter
    @Setter
    @Size(min = 3, max = 32)
    @Pattern(regexp = RegExpUtils.USERNAME)
    @ApiModelProperty("登录用户名")
    private String username;

    @Getter
    @Setter
    @NotEmpty
    @Size(min = 6, max = 32)
    @ApiModelProperty("登录密码")
    private String password;
}
