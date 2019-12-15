package net.codelet.cloud.notification.query.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
    name = "${services.notification.query.name:notification-query}",
    contextId = "notification-query"
)
public interface NotificationQueryApi {
}
