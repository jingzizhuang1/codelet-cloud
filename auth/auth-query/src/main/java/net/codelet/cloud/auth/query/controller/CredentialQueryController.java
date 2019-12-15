package net.codelet.cloud.auth.query.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.auth.query.api.CredentialQueryApi;
import net.codelet.cloud.auth.query.service.CredentialQueryService;
import net.codelet.cloud.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"domain=登录认证", "biz=认证凭证", "responsibility=查询"})
public class CredentialQueryController extends BaseController implements CredentialQueryApi {

    private final CredentialQueryService credentialQueryService;

    @Autowired
    public CredentialQueryController(
        CredentialQueryService credentialQueryService
    ) {
        this.credentialQueryService = credentialQueryService;
    }

    @Override
    @ApiOperation("检查认证凭证是否可用于注册")
    public Boolean available(
        @ApiParam("认证凭证，如电子邮箱地址、手机号码或登录用户名等") String credential
    ) {
        return !credentialQueryService.exists(credential);
    }
}
