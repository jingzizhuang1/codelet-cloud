package net.codelet.cloud.parameter.command.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.parameter.command.api.ParameterCommandApi;
import net.codelet.cloud.parameter.command.dto.ParameterSetDTO;
import net.codelet.cloud.parameter.command.service.ParameterCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@RestController
@Api(tags = {"domain=业务参数", "biz=业务参数", "responsibility=命令"})
public class ParameterCommandController extends BaseController implements ParameterCommandApi {

    private static final String NAME_PATTERN = "^[a-z](-?[0-9a-z]+)*$";

    private final ParameterCommandService parameterCommandService;

    @Autowired
    public ParameterCommandController(
        ParameterCommandService parameterCommandService
    ) {
        this.parameterCommandService = parameterCommandService;
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("设置业务参数")
    public void setParameter(
        @ApiParam("应用 ID") @Pattern(regexp = NAME_PATTERN) @Size(max = 45) String appId,
        @ApiParam("参数名") @Pattern(regexp = NAME_PATTERN) @Size(max = 45) String parameterName,
        @ApiParam("修订版本号") Long revision,
        @Valid ParameterSetDTO parameterDTO
    ) {
        parameterCommandService.set(getOperator(), appId == null ? "" : appId, parameterName, revision, parameterDTO);
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("删除业务参数")
    public void deleteParameter(
        @ApiParam("应用 ID") @Pattern(regexp = NAME_PATTERN) @Size(max = 45) String appId,
        @ApiParam("参数名") @Pattern(regexp = NAME_PATTERN) @Size(max = 45) String parameterName,
        @ApiParam("修订版本号") long revision
    ) {
        parameterCommandService.delete(getOperator(), appId == null ? "" : appId, parameterName, revision);
    }
}
