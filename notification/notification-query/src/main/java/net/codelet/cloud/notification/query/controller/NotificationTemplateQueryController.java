package net.codelet.cloud.notification.query.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.SetReferencedEntities;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.notification.query.api.NotificationTemplateQueryApi;
import net.codelet.cloud.notification.query.dto.NotificationTemplateGetDTO;
import net.codelet.cloud.notification.query.dto.NotificationTemplateQueryDTO;
import net.codelet.cloud.notification.query.entity.NotificationTemplateQueryEntity;
import net.codelet.cloud.notification.query.entity.NotificationTemplateWithContentsQueryEntity;
import net.codelet.cloud.notification.query.service.NotificationTemplateQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = {"domain=通知", "biz=通知消息模版", "responsibility=查询"})
public class NotificationTemplateQueryController extends BaseController implements NotificationTemplateQueryApi {

    private final NotificationTemplateQueryService notificationTemplateQueryService;

    @Autowired
    public NotificationTemplateQueryController(NotificationTemplateQueryService notificationTemplateQueryService) {
        this.notificationTemplateQueryService = notificationTemplateQueryService;
    }

    @Override
    @SetReferencedEntities
    @ApiOperation("查询模版")
    public Page<NotificationTemplateQueryEntity> search(
        @Valid NotificationTemplateQueryDTO queryDTO
    ) {
        return notificationTemplateQueryService.search(queryDTO);
    }

    @Override
    @SetReferencedEntities
    @ApiOperation("取得通知消息模版详细信息")
    public NotificationTemplateWithContentsQueryEntity get(
        @ApiParam("模版 ID") String templateId,
        @Valid NotificationTemplateGetDTO queryDTO
    ) {
        return notificationTemplateQueryService.get(templateId, queryDTO);
    }

    @Override
    @ApiOperation("检查模版是否存在")
    public boolean exists(
        @ApiParam("模版 ID") String templateId,
        @ApiParam("是否已被停用") Boolean disabled
    ) {
        return notificationTemplateQueryService.exists(templateId, disabled);
    }
}
