package net.codelet.cloud.notification.query.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.SetReferencedEntities;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.notification.query.api.NotificationTemplateContentQueryApi;
import net.codelet.cloud.notification.query.dto.NotificationTemplateContentQueryDTO;
import net.codelet.cloud.notification.query.entity.NotificationTemplateContentQueryEntity;
import net.codelet.cloud.notification.query.service.NotificationTemplateContentQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = {"domain=通知", "biz=通知消息模版内容", "responsibility=查询"})
public class NotificationTemplateContentQueryController extends BaseController implements NotificationTemplateContentQueryApi {

    private final NotificationTemplateContentQueryService notificationTemplateQueryService;

    @Autowired
    public NotificationTemplateContentQueryController(
        NotificationTemplateContentQueryService notificationTemplateQueryService
    ) {
        this.notificationTemplateQueryService = notificationTemplateQueryService;
    }

    @Override
    @SetReferencedEntities
    @ApiOperation("查询通知消息模版内容")
    public Page<NotificationTemplateContentQueryEntity> search(
        @ApiParam("模版 ID") String templateId,
        @Valid NotificationTemplateContentQueryDTO queryDTO
    ) {
        return notificationTemplateQueryService.search(templateId, queryDTO);
    }

    @Override
    @SetReferencedEntities
    @ApiOperation("取得通知消息模版内容详细信息")
    public NotificationTemplateContentQueryEntity get(
        @ApiParam("模版 ID") String templateId,
        @ApiParam("语言代码") String languageCode
    ) {
        return notificationTemplateQueryService.get(templateId, languageCode);
    }
}
