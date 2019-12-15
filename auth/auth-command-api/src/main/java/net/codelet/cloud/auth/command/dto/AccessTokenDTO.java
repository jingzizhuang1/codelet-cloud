package net.codelet.cloud.auth.command.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.auth.command.entity.AccessTokenCommandEntity;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.dto.ContextDTO;
import net.codelet.cloud.error.AccessTokenInvalidError;
import net.codelet.cloud.util.StringUtils;
import net.codelet.cloud.vo.UserType;

/**
 * 用户访问令牌内容数据传输对象。
 * 用户访问令牌内容格式：
 *   第 1 行 - 访问令牌 ID
 *   第 2 行 - 用户 ID
 *   第 3 行 - 用户类型
 *   第 4 行 - 用户 LOGO
 *   第 5 行 - 用户姓名
 *   第 6 行 - 用户信息最后更新时间（三十六进制毫秒）
 *   第 7 行 - 登录远程 IP 地址（十六进制）
 *   第 8 行 - 客户端信息
 */
public class AccessTokenDTO extends BaseDTO {

    private static final long serialVersionUID = 4913523292853291517L;

    // 用户访问令牌装载数据中各字段之间的分隔符
    private static final String SEPARATOR = "\r\n";

    @Getter
    @Setter
    @ApiModelProperty("用户访问令牌在数据库中的 ID")
    private String id;

    @Getter
    @Setter
    @ApiModelProperty("用户 ID")
    private String userId;

    @Getter
    @Setter
    @ApiModelProperty("用户类型")
    private UserType type;

    @Getter
    @Setter
    @ApiModelProperty("用户头像")
    private String logo;

    @Getter
    @Setter
    @ApiModelProperty("用户姓名")
    private String name;

    @Getter
    @Setter
    @ApiModelProperty("用户信息更新版本号")
    private Long userRevision;

    @Getter
    @Setter
    @ApiModelProperty("初次登录时的 IP 地址")
    private String remoteAddr;

    @Getter
    @Setter
    @ApiModelProperty("客户端信息")
    private String client;

    /**
     * 构造方法，用于生成访问令牌。
     * @param context           请求上下文
     * @param user              用户信息
     * @param accessTokenEntity 访问令牌数据实体
     */
    public AccessTokenDTO(
        ContextDTO context,
        UserDTO user,
        AccessTokenCommandEntity accessTokenEntity
    ) {
        this.id = accessTokenEntity.getId();
        this.userId = user.getId();
        this.type = user.getType();
        this.logo = user.getLogo();
        this.name = user.getName();
        this.userRevision = user.getRevision();
        this.remoteAddr = context.getRemoteAddr();
        this.client = context.getClient();
    }

    /**
     * 构造方法，用于解析访问令牌。
     * @param payload 访问令牌内容
     */
    public AccessTokenDTO(final String payload) {
        String[] fields = payload.split(SEPARATOR);
        if (fields.length != 8) {
            throw new AccessTokenInvalidError();
        }
        this.id = fields[0];
        this.userId = fields[1];
        this.type = UserType.getByName(fields[2]);
        this.logo = fields[3];
        this.name = fields[4];
        this.userRevision = Long.parseLong(fields[5], 36);
        this.remoteAddr = fields[6];
        this.client = fields[7];
    }

    /**
     * 转为 JWT 中装载的数据。
     * @return JWT 中装载的数据
     */
    @Override
    public String toString() {
        return this.id
            + SEPARATOR + this.userId
            + SEPARATOR + this.type.name()
            + SEPARATOR + StringUtils.trim(this.logo)
            + SEPARATOR + StringUtils.trim(this.name)
            + SEPARATOR + Long.toString(this.getUserRevision(), 36)
            + SEPARATOR + this.remoteAddr
            + SEPARATOR + this.client;
    }

    /**
     * 用户信息。
     */
    public static class UserDTO extends BaseDTO {

        private static final long serialVersionUID = -8609386943996849908L;

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
        @ApiModelProperty("姓名")
        private String name;

        @Getter
        @Setter
        @ApiModelProperty("头像")
        private String logo;

        @Getter
        @Setter
        @JsonIgnore
        @ApiModelProperty("更新版本号")
        private Long revision;
    }
}
