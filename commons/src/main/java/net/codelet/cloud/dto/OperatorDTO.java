package net.codelet.cloud.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.vo.UserType;

/**
 * 操作者信息。
 */
public class OperatorDTO extends BaseDTO {

    private static final long serialVersionUID = -4168966237747607187L;

    @Getter
    @Setter
    @ApiModelProperty("用户 ID")
    private String id;

    @Getter
    @Setter
    @ApiModelProperty("用户类型")
    private UserType type;

    @Getter
    @Setter
    @ApiModelProperty("头像地址")
    private String logo;

    @Getter
    @Setter
    @ApiModelProperty("姓名")
    private String name;

    @Getter
    @Setter
    @JsonIgnore
    @ApiModelProperty("访问令牌 ID")
    private String accessTokenId;

    @Getter
    @Setter
    @JsonIgnore
    @ApiModelProperty("客户端远程 IP 地址")
    private String remoteAddr;

    @Getter
    @Setter
    @JsonIgnore
    @ApiModelProperty("用户代理字符串 ID")
    private String userAgentId;

    @Getter
    @Setter
    @JsonIgnore
    @ApiModelProperty("刷新后的访问令牌")
    private String renewedAccessToken;

    public OperatorDTO() {
    }

    public OperatorDTO(String id) {
        this.id = id;
    }

    public OperatorDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isAdministrator() {
        return type != null && type.isAdministrator();
    }
}
