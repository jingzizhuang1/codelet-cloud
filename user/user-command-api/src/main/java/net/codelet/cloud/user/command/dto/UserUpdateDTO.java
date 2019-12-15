package net.codelet.cloud.user.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.util.RegExpUtils;
import net.codelet.cloud.vo.Gender;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class UserUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = 1220075046218631571L;

    @Getter
    @Setter
    @NotBlank
    @Size(min = 2, max = 45)
    @ApiModelProperty("姓名")
    private String name;

    @Getter
    @Setter
    @Pattern(regexp = RegExpUtils.EMAIL)
    @ApiModelProperty("电子邮箱地址")
    private String email;

    @Getter
    @Setter
    @Pattern(regexp = RegExpUtils.MOBILE)
    @ApiModelProperty("手机号码")
    private String mobile;

    @Getter
    @Setter
    @ApiModelProperty("出生日期")
    private Date birthDate;

    @Getter
    @Setter
    @ApiModelProperty("性别")
    private Gender gender;
}
