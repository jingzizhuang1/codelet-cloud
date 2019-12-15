package net.codelet.cloud.user.query.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.annotation.InternalAccessOnly;
import net.codelet.cloud.annotation.SetReferencedEntities;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.user.query.api.UserQueryApi;
import net.codelet.cloud.user.query.dto.UserQueryDTO;
import net.codelet.cloud.user.query.entity.UserQueryEntity;
import net.codelet.cloud.user.query.service.UserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@Api(tags = {"domain=用户", "biz=用户", "responsibility=查询"})
public class UserQueryController extends BaseController implements UserQueryApi {

    private final UserQueryService userQueryService;

    @Autowired
    public UserQueryController(
        UserQueryService userQueryService
    ) {
        this.userQueryService = userQueryService;
    }

    @Override
    @CheckUserPrivilege
    @ApiOperation("取得登录用户信息")
    public UserQueryEntity get() {
        return get(getOperator().getId());
    }

    @Override
    @ApiOperation("取得用户基本信息")
    public UserQueryEntity get(
        @ApiParam("用户 ID") String userId
    ) {
        return userQueryService.get(userId);
    }

    @Override
    @SetReferencedEntities
    @ApiOperation("查询用户")
    public Page<UserQueryEntity> search(
        @Valid UserQueryDTO queryDTO
    ) {
        return userQueryService.search(queryDTO);
    }

    @Override
    @InternalAccessOnly
    @ApiOperation("批量取得用户信息")
    public List<UserQueryEntity> batchGet(
        @ApiParam("用户 ID 集合") Set<String> entityIDs
    ) {
        return userQueryService.get(entityIDs);
    }
}
