package net.codelet.cloud.role.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 角色组成员加入操作表单。
 */
public class RoleMemberJoinDTO extends BaseDTO {

    private static final long serialVersionUID = -7595827064871628898L;

    @Getter
    @Setter
    @NotNull
    @Size(min = 1, max = 50)
    @ApiModelProperty("用户 ID/职员 ID")
    private Set<String> memberIDs;
}
