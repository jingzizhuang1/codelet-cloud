package net.codelet.cloud.notification.command.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.notification.command.api.MailConfigurationCommandApi;
import net.codelet.cloud.notification.command.dto.MailConfigurationCreateDTO;
import net.codelet.cloud.notification.command.dto.MailConfigurationUpdateDTO;
import net.codelet.cloud.notification.command.service.MailConfigurationCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = {"domain=通知", "biz=电子邮件发送配置", "responsibility=命令"})
public class MailConfigurationCommandController extends BaseController implements MailConfigurationCommandApi {

    private final MailConfigurationCommandService mailConfigurationCommandService;

    @Autowired
    public MailConfigurationCommandController(
        MailConfigurationCommandService mailConfigurationCommandService
    ) {
        this.mailConfigurationCommandService = mailConfigurationCommandService;
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("设置电子邮件发送配置")
    public void create(@Valid MailConfigurationCreateDTO createDTO) {
        mailConfigurationCommandService.create(getOperator(), createDTO);
    }

    @Override
    @ApiOperation("更新电子邮件发送配置")
    public void update(
        @ApiParam("发送配置 ID") String configurationId,
        @ApiParam(value = "修订版本号", required = true) long revision,
        @Valid MailConfigurationUpdateDTO updateDTO
    ) {
        mailConfigurationCommandService.update(getOperator(), configurationId, updateDTO, revision);
    }

    @Override
    @ApiOperation("将电子邮件发送配置设置为默认配置")
    public void setAsDefault(
        @ApiParam("发送配置 ID") String configurationId,
        @ApiParam(value = "修订版本号", required = true) long revision
    ) {
        mailConfigurationCommandService.setAsDefault(getOperator(), configurationId, revision);
    }

    @Override
    @ApiOperation("停用电子邮件发送配置")
    public void disable(
        @ApiParam("发送配置 ID") String configurationId,
        @ApiParam(value = "修订版本号", required = true) long revision
    ) {
        mailConfigurationCommandService.setDisabled(getOperator(), configurationId, revision, true);
    }

    @Override
    @ApiOperation("启用电子邮件发送配置")
    public void enable(
        @ApiParam("发送配置 ID") String configurationId,
        @ApiParam(value = "修订版本号", required = true) long revision
    ) {
        mailConfigurationCommandService.setDisabled(getOperator(), configurationId, revision, false);
    }

    @Override
    @ApiOperation("删除电子邮件发送配置")
    public void delete(
        @ApiParam("发送配置 ID") String configurationId,
        @ApiParam(value = "修订版本号", required = true) long revision
    ) {
        mailConfigurationCommandService.delete(getOperator(), configurationId, revision);
    }
}
