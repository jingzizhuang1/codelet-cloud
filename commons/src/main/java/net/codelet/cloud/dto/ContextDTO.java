package net.codelet.cloud.dto;

import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.constant.HttpResponseHeaders;
import net.codelet.cloud.entity.BaseVersionedEntity;
import net.codelet.cloud.error.AccessTokenExpiredError;
import net.codelet.cloud.error.AccessTokenInvalidError;
import net.codelet.cloud.util.RemoteAddressUtils;
import net.codelet.cloud.util.UserAgentUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.ApiModelProperty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.USER_AGENT;

/**
 * 请求上下文对象。
 */
public class ContextDTO extends BaseDTO {

    private static final long serialVersionUID = -7530347688214078329L;

    @Getter
    @JsonIgnore
    @ApiModelProperty("HTTP 请求实例")
    private HttpServletRequest request;

    @Getter
    @JsonIgnore
    @ApiModelProperty("HTTP 响应实例")
    private HttpServletResponse response;

    @Getter
    @Setter
    @ApiModelProperty("客户端信息（取自 User-Agent 请求头）")
    private String client;

    @Getter
    @ApiModelProperty("客户端用户代理字符串（取自 User-Agent 请求头）")
    private String userAgent;

    @Getter
    @ApiModelProperty("客户端远程 IP 地址")
    private String remoteAddr;

    @Getter
    @ApiModelProperty("授权信息（取自 Authorization 请求头）")
    private String authorization;

    @Getter
    @ApiModelProperty("授权用户信息")
    private OperatorDTO operator;

    @Getter
    @ApiModelProperty("访问令牌")
    private String accessToken;

    @Getter
    @ApiModelProperty("是否已完成初始化")
    private boolean initialized = false;

    public ContextDTO() {
    }

    public ContextDTO(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        this.init(request, response);
    }

    public ContextDTO(
        String userAgent,
        String remoteAddr,
        String authorization
    ) {
        this.userAgent = userAgent;
        this.remoteAddr = remoteAddr;
        this.authorization = authorization;
    }

    /**
     * 初始化上下文对象。
     * @param request HTTP 请求实例
     * @param response HTTP 响应实例
     */
    public void init(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        this.request = request;
        this.response = response;

        if (request != null) {
            this.client = UserAgentUtils.client(request.getHeader(USER_AGENT));
            this.userAgent = UserAgentUtils.compact(request.getHeader(USER_AGENT));
            this.remoteAddr = RemoteAddressUtils.getRemoteAddr(request);
            String[] values = request.getParameterMap().get("access-token");
            if (values != null && values.length > 0) {
                this.authorization = this.claimTemporaryAccessToken(values[values.length - 1]);
                request.setAttribute(AUTHORIZATION, this.authorization);
            } else {
                this.authorization = request.getHeader(AUTHORIZATION);
            }
        }

        this.initialized = true;
    }

    public void setOperator(OperatorDTO operator) {
        this.operator = operator;
        if (operator != null) {
            this.accessToken = operator.getRenewedAccessToken();
        }
    }

    @JsonIgnore
    public void setRevision(BaseVersionedEntity domainObject) {
        if (domainObject == null) {
            return;
        }
        setRevision(domainObject.getRevision());
    }

    @JsonIgnore
    public void setRevision(Long revision) {
        if (this.response == null || revision == null) {
            return;
        }
        this.response.setHeader(HttpResponseHeaders.DATA_REVISION, revision.toString());
    }

    /**
     * 生成临时访问令牌。
     * @param expiresAt 有效截止时间
     * @return 临时访问令牌
     */
    public String generateTemporaryAccessToken(Date expiresAt) {
        return Jwts
            .builder()
            .setSubject(this.getAuthorization())
            .signWith(
                SignatureAlgorithm.HS384,
                this.getUserAgent()
            )
            .setExpiration(expiresAt)
            .compact();
    }

    /**
     * 校验临时访问令牌。
     * @param temporaryAccessToken 临时访问令牌
     * @return 访问令牌
     */
    public String claimTemporaryAccessToken(String temporaryAccessToken) {
        try {
            return Jwts
                .parser()
                .setSigningKey(this.getUserAgent())
                .parseClaimsJws(temporaryAccessToken)
                .getBody()
                .getSubject();
        } catch (final ExpiredJwtException e) {
            throw new AccessTokenExpiredError();
        } catch (final JwtException e) {
            throw new AccessTokenInvalidError();
        }
    }
}
