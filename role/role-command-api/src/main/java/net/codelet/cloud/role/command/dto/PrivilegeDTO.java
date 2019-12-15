package net.codelet.cloud.role.command.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.vo.Permission;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 权限。
 */
public class PrivilegeDTO extends BaseDTO {

    private static final long serialVersionUID = 5687969672980207106L;

    @Getter
    @Setter
    @NotBlank
    @ApiModelProperty("适用领域")
    private String scope;

    @Getter
    @Setter
    @NotNull
    @ApiModelProperty("访问许可")
    private Permission permission;

    @JsonIgnore
    public String privilege() {
        return scope + ":" + permission.name();
    }
}
