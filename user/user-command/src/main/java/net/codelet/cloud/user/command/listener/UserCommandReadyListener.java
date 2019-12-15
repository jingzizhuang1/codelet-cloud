package net.codelet.cloud.user.command.listener;

import net.codelet.cloud.user.command.service.UserCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * 应用初始化。
 */
@Configuration
public class UserCommandReadyListener {

    private final UserCommandService userCommandService;

    @Autowired
    public UserCommandReadyListener(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    /**
     * 应用启动后若尚未创建系统用户账号则执行系统用户账号创建处理。
     * @param event ApplicationReadyEvent
     */
    @EventListener
    public void createSystemUsers(ApplicationReadyEvent event) {
        userCommandService.createSystemUsers();
    }
}
