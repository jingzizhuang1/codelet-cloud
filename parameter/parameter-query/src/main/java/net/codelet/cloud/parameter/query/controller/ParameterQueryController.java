package net.codelet.cloud.parameter.query.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.SetReferencedEntities;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.parameter.query.api.ParameterQueryApi;
import net.codelet.cloud.parameter.query.entity.ParameterQueryEntity;
import net.codelet.cloud.parameter.query.service.ParameterQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = {"domain=业务参数", "biz=业务参数", "responsibility=查询"})
public class ParameterQueryController extends BaseController implements ParameterQueryApi {

    private final ParameterQueryService parameterQueryService;

    @Autowired
    public ParameterQueryController(
        ParameterQueryService parameterQueryService
    ) {
        this.parameterQueryService = parameterQueryService;
    }

    @Override
    @SetReferencedEntities
    @ApiOperation("取得所有应用业务参数")
    public List<ParameterQueryEntity> list(String appId) {
        return parameterQueryService.list(appId == null ? "" : appId);
    }

    @Override
    @ApiOperation("取得参数值")
    public String getValue(
        @ApiParam("应用 ID") String appId,
        @ApiParam("参数名") String parameterName
    ) {
        return parameterQueryService.getValue(appId == null ? "" : appId, parameterName);
    }

    @Override
    @SetReferencedEntities
    @ApiOperation("取得参数信息")
    public ParameterQueryEntity get(String appId, String parameterName) {
        ParameterQueryEntity parameterEntity = parameterQueryService
            .get(appId == null ? "" : appId, parameterName);
        if (parameterEntity == null) {
            throw new NotFoundError();
        }
        return parameterEntity;
    }
}
