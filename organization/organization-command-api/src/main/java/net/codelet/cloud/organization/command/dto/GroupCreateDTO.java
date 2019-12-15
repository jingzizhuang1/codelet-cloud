package net.codelet.cloud.organization.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * 工作组信息创建数据传输对象。
 */
public class GroupCreateDTO extends OrganizationUpdateDTO implements OrganizationCreateDTO {

    private static final long serialVersionUID = -7106224579645106190L;

    @Getter
    @Setter
    @ApiModelProperty("管理员用户 ID 列表")
    private Set<String> administratorIDs;
}
