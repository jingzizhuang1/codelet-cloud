package net.codelet.cloud.notification.command.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.notification.command.api.SmsConfigurationCommandApi;
import net.codelet.cloud.notification.command.dto.SmsConfigurationCreateDTO;
import net.codelet.cloud.notification.command.dto.SmsConfigurationUpdateDTO;
import net.codelet.cloud.notification.command.service.SmsConfigurationCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = {"domain=通知", "biz=短信发送配置", "responsibility=命令"})
public class SmsConfigurationCommandController extends BaseController implements SmsConfigurationCommandApi {

    private final SmsConfigurationCommandService smsConfigurationCommandService;

    @Autowired
    public SmsConfigurationCommandController(
        SmsConfigurationCommandService smsConfigurationCommandService
    ) {
        this.smsConfigurationCommandService = smsConfigurationCommandService;
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("设置短信发送配置")
    public void create(@Valid SmsConfigurationCreateDTO createDTO) {
        smsConfigurationCommandService.create(getOperator(), createDTO);
    }

    @Override
    @ApiOperation("更新短信发送配置")
    public void update(
        @ApiParam("发送配置 ID") String configurationId,
        @ApiParam(value = "修订版本号", required = true) long revision,
        @Valid SmsConfigurationUpdateDTO updateDTO
    ) {
        smsConfigurationCommandService.update(getOperator(), configurationId, updateDTO, revision);
    }

    @Override
    @ApiOperation("将短信发送配置设置为默认配置")
    public void setAsDefault(
        @ApiParam("发送配置 ID") String configurationId,
        @ApiParam(value = "修订版本号", required = true) long revision
    ) {
        smsConfigurationCommandService.setAsDefault(getOperator(), configurationId, revision);
    }

    @Override
    @ApiOperation("停用短信发送配置")
    public void disable(
        @ApiParam("发送配置 ID") String configurationId,
        @ApiParam(value = "修订版本号", required = true) long revision
    ) {
        smsConfigurationCommandService.setDisabled(getOperator(), configurationId, revision, true);
    }

    @Override
    @ApiOperation("启用短信发送配置")
    public void enable(
        @ApiParam("发送配置 ID") String configurationId,
        @ApiParam(value = "修订版本号", required = true) long revision
    ) {
        smsConfigurationCommandService.setDisabled(getOperator(), configurationId, revision, false);
    }

    @Override
    @ApiOperation("删除短信发送配置")
    public void delete(
        @ApiParam("发送配置 ID") String configurationId,
        @ApiParam(value = "修订版本号", required = true) long revision
    ) {
        smsConfigurationCommandService.delete(getOperator(), configurationId, revision);
    }
}
