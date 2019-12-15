package net.codelet.cloud.notification.query.controller;

import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.notification.query.api.NotificationQueryApi;
import net.codelet.cloud.notification.query.service.NotificationQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = {"domain=通知", "biz=通知消息", "responsibility=查询"})
public class NotificationQueryController extends BaseController implements NotificationQueryApi {

    private final NotificationQueryService notificationQueryService;

    @Autowired
    public NotificationQueryController(
        NotificationQueryService notificationQueryService
    ) {
        this.notificationQueryService = notificationQueryService;
    }

}
