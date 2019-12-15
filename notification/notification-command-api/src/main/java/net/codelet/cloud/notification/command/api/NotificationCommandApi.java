package net.codelet.cloud.notification.command.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
    name = "${services.notification.command.name:notification-command}",
    contextId = "notification-command"
)
public interface NotificationCommandApi {
}
