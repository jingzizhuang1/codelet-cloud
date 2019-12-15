package net.codelet.cloud.notification.query.service.impl;

import net.codelet.cloud.notification.query.repository.NotificationQueryRepository;
import net.codelet.cloud.notification.query.service.NotificationQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationQueryRepository notificationQueryRepository;

    @Autowired
    public NotificationQueryServiceImpl(NotificationQueryRepository notificationQueryRepository) {
        this.notificationQueryRepository = notificationQueryRepository;
    }

}
