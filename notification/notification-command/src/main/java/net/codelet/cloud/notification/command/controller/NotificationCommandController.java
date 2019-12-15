package net.codelet.cloud.notification.command.controller;

import io.swagger.annotations.Api;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.notification.command.api.NotificationCommandApi;
import net.codelet.cloud.notification.command.service.NotificationCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"domain=通知", "biz=通知消息", "responsibility=命令"})
public class NotificationCommandController extends BaseController implements NotificationCommandApi {

    private final NotificationCommandService notificationCommandService;

    @Autowired
    public NotificationCommandController(
        NotificationCommandService notificationCommandService
    ) {
        this.notificationCommandService = notificationCommandService;
    }

}
