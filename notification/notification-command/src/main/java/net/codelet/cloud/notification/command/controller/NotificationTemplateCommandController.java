package net.codelet.cloud.notification.command.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.notification.command.api.NotificationTemplateCommandApi;
import net.codelet.cloud.notification.command.dto.NotificationTemplateContentDTO;
import net.codelet.cloud.notification.command.dto.NotificationTemplateContentSaveDTO;
import net.codelet.cloud.notification.command.dto.NotificationTemplateCreateDTO;
import net.codelet.cloud.notification.command.dto.NotificationTemplateUpdateDTO;
import net.codelet.cloud.notification.command.entity.NotificationTemplateCommandEntity;
import net.codelet.cloud.notification.command.service.NotificationTemplateCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 通知消息模版 REST 控制器。
 */
@RestController
@Api(tags = {"domain=通知", "biz=通知消息模版", "responsibility=命令"})
public class NotificationTemplateCommandController extends BaseController implements NotificationTemplateCommandApi {

    private final NotificationTemplateCommandService notificationTemplateCommandService;

    @Autowired
    public NotificationTemplateCommandController(
        NotificationTemplateCommandService notificationTemplateCommandService
    ) {
        this.notificationTemplateCommandService = notificationTemplateCommandService;
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("创建通知消息模版")
    public NotificationTemplateCommandEntity create(
        @Valid NotificationTemplateCreateDTO createDTO
    ) {
        return notificationTemplateCommandService.create(getOperator(), createDTO);
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("更新通知消息模版")
    public void update(
        @ApiParam("模版 ID") String templateId,
        @ApiParam(value = "修订版本号", required = true) long revision,
        @Valid NotificationTemplateUpdateDTO updateDTO
    ) {
        notificationTemplateCommandService
            .update(getOperator(), templateId, revision, updateDTO);
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("停用通知消息模版")
    public void disable(
        @ApiParam("模版 ID") String templateId,
        @ApiParam(value = "修订版本号", required = true) long revision
    ) {
        notificationTemplateCommandService
            .disable(getOperator(), templateId, revision);
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("启用通知消息模版")
    public void enable(
        @ApiParam("模版 ID") String templateId,
        @ApiParam(value = "修订版本号", required = true) long revision
    ) {
        notificationTemplateCommandService
            .enable(getOperator(), templateId, revision);
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("删除通知消息模版")
    public void delete(
        @ApiParam("模版 ID") String templateId,
        @ApiParam(value = "修订版本号", required = true) long revision
    ) {
        notificationTemplateCommandService
            .delete(getOperator(), templateId, revision);
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("设置通知消息模版语言内容")
    public void setLanguage(
        @ApiParam("模版 ID") String templateId,
        @ApiParam("语言代码") String languageCode,
        @ApiParam("修订版本号") long revision,
        @Valid NotificationTemplateContentSaveDTO contentSaveDTO
    ) {
        NotificationTemplateContentDTO contentDTO = new NotificationTemplateContentDTO();
        contentDTO.setLanguageCode(languageCode);
        contentDTO.setContentType(contentSaveDTO.getContentType());
        contentDTO.setContent(contentSaveDTO.getContent());
        notificationTemplateCommandService
            .saveLanguageContent(getOperator(), templateId, revision, contentDTO);
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("停用通知消息模版语言内容")
    public void disableLanguage(
        @ApiParam("模版 ID") String templateId,
        @ApiParam("语言代码") String languageCode,
        @ApiParam(value = "修订版本号", required = true) long revision
    ) {
        notificationTemplateCommandService
            .disableLanguage(getOperator(), templateId, languageCode, revision);
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("启用通知消息模版语言内容")
    public void enableLanguage(
        @ApiParam("模版 ID") String templateId,
        @ApiParam("语言代码") String languageCode,
        @ApiParam(value = "修订版本号", required = true) long revision
    ) {
        notificationTemplateCommandService
            .enableLanguage(getOperator(), templateId, languageCode, revision);
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @ApiOperation("删除通知消息模版语言内容")
    public void deleteLanguage(
        @ApiParam("模版 ID") String templateId,
        @ApiParam("语言代码") String languageCode,
        @ApiParam(value = "修订版本号", required = true) long revision
    ) {
        notificationTemplateCommandService
            .deleteLanguage(getOperator(), templateId, languageCode, revision);
    }
}
