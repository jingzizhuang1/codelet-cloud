package net.codelet.cloud.starters.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@Configuration
public class ApplicationEventListener {

    private final Environment environment;

    @Autowired
    public ApplicationEventListener(Environment environment) {
        this.environment = environment;
    }

    /**
     * 应用启动完成后的处理。
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println(String.format(
            "\n\u001B[1;97;44m %s starterd. \u001B[0m\n",
            environment.getProperty("spring.application.name")
        ));
    }
}
