package net.codelet.cloud.error;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * 权限不足错误。
 */
public class NoPrivilegeError extends AccessDeniedError {

    private static final long serialVersionUID = -2110123033308023062L;

    @Getter
    @ApiModelProperty("所需权限")
    private Set<String> privileges;

    public NoPrivilegeError() {
        super("error.no-privilege");
    }

    public NoPrivilegeError(Set<String> privileges) {
        this();
        this.privileges = privileges;
    }

    public NoPrivilegeError(Enum[] privileges) {
        this();
        Set<String> privilegeSet = new HashSet<>();
        for (Enum privilege : privileges) {
            privilegeSet.add(privilege.name());
        }
        this.privileges = privilegeSet;
    }
}
