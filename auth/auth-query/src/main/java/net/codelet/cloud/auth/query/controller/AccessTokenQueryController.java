package net.codelet.cloud.auth.query.controller;

import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.auth.query.api.AccessTokenQueryApi;
import net.codelet.cloud.auth.query.service.AccessTokenQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = {"domain=登录认证", "biz=访问令牌", "responsibility=查询"})
public class AccessTokenQueryController extends BaseController implements AccessTokenQueryApi {

    private final AccessTokenQueryService accessTokenQueryService;

    @Autowired
    public AccessTokenQueryController(
        AccessTokenQueryService accessTokenQueryService
    ) {
        this.accessTokenQueryService = accessTokenQueryService;
    }

}
