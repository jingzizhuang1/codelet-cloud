package net.codelet.cloud.organization.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * 事业部信息创建数据传输对象。
 */
public class DivisionCreateDTO extends DivisionUpdateDTO implements OrganizationCreateDTO {

    private static final long serialVersionUID = 4055207068839427779L;

    @Getter
    @Setter
    @ApiModelProperty("管理员用户 ID 列表")
    private Set<String> administratorIDs;
}
