package net.codelet.cloud.auth.command.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.InternalAccessOnly;
import net.codelet.cloud.annotation.ValidateVerificationCode;
import net.codelet.cloud.auth.command.api.AuthenticationApi;
import net.codelet.cloud.auth.command.dto.AuthenticateDTO;
import net.codelet.cloud.auth.command.service.AccessTokenCommandService;
import net.codelet.cloud.auth.command.service.AuthenticationService;
import net.codelet.cloud.constant.HttpResponseHeaders;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.error.AuthenticationError;
import net.codelet.cloud.error.ValidationError;
import net.codelet.cloud.user.command.api.UserCommandApi;
import net.codelet.cloud.user.entity.UserBaseEntity;
import net.codelet.cloud.util.StringUtils;
import net.codelet.cloud.vo.VerificationPurpose;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@Api(tags = {"domain=登录认证", "biz=权限鉴定", "responsibility=命令"})
public class AuthenticationController extends BaseController implements AuthenticationApi {

    private final AuthenticationService authenticationService;
    private final AccessTokenCommandService accessTokenCommandService;
    private final UserCommandApi userCommandApi;

    @Autowired
    public AuthenticationController(
        AuthenticationService authenticationService,
        AccessTokenCommandService accessTokenCommandService,
        UserCommandApi userCommandApi
    ) {
        this.authenticationService = authenticationService;
        this.accessTokenCommandService = accessTokenCommandService;
        this.userCommandApi = userCommandApi;
    }

    @Override
    @ValidateVerificationCode(
        value = VerificationPurpose.USER_SIGN_IN,
        passWhenParameterPresent = {
            @ValidateVerificationCode.Parameter(
                type = AuthenticateDTO.class,
                propertyName = "password"
            )
        }
    )
    @ApiOperation(
        value = "验证登录凭证，生成访问令牌",
        notes = "通过认证后服务器将通过 <code>X-Access-Token</code> 响应头返回 JWT 访问令牌。"
            + "客户端需要在后续请求中通过 <code>Authorization</code> 请求头携带访问令牌。"
    )
    public void authenticate(
        @Valid AuthenticateDTO authenticateDTO
    ) {
        // 若未设置电子邮件验证码或短信验证码则必须设置登录密码
        if (StringUtils.isEmpty(authenticateDTO.getPassword())
            && StringUtils.isEmpty(authenticateDTO.getEmailVerificationCode())
            && StringUtils.isEmpty(authenticateDTO.getSmsVerificationCode())
        ) {
            throw new ValidationError("error.validation.password-is-required"); // TODO: reformat error
        }

        HttpServletResponse response = getResponse();
        if (response == null) {
            return;
        }

        // 鉴定用户提供的认证信息，当使用验证码登录时，若认证凭证不存在则创建新的用户
        UserBaseEntity user;
        try {
            user = authenticationService.authenticate(authenticateDTO);
        } catch (AuthenticationError e) {
            if (!StringUtils.isEmpty(authenticateDTO.getPassword())) {
                throw e;
            }
            user = userCommandApi.create(authenticateDTO.getUsername());
        }

        // 验证登录凭证，生成令牌，并通过响应头返回
        response.setHeader(
            HttpResponseHeaders.ACCESS_TOKEN,
            accessTokenCommandService.create(getContext(), user)
        );
        response.setHeader(
            HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
            HttpResponseHeaders.ACCESS_TOKEN
        );
    }

    @Override
    @InternalAccessOnly
    @ApiOperation("刷新访问令牌（仅限服务间调用）")
    public String renew(
        @ApiParam("所有者用户 ID") String userId,
        @ApiParam("访问令牌 ID") String accessTokenId
    ) {
        return accessTokenCommandService.renew(getContext(), userId, accessTokenId);
    }

    @Override
    @ApiOperation("销毁访问令牌")
    public void destroy(
        @ApiParam("访问令牌") String accessToken
    ) {
    }
}
