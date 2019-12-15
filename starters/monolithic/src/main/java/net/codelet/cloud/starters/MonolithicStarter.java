package net.codelet.cloud.starters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.bus.BusAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {
    SecurityAutoConfiguration.class,
    BusAutoConfiguration.class
})
@EntityScan({"net.codelet.cloud"})
@EnableJpaRepositories({"net.codelet.cloud"})
@ComponentScan({"net.codelet.cloud"})
public class MonolithicStarter {
    public static void main(String[] args) {
        // 启动应用，生成 PID 文件，取得上下文对象
        SpringApplication application = new SpringApplication(MonolithicStarter.class);
        application.addListeners(new ApplicationPidFileWriter());
        application.run(args);
    }
}
