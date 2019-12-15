package net.codelet.cloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置中心服务启动类。
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan({"net.codelet.cloud"})
@EnableConfigServer
@EnableDiscoveryClient
@Configuration
@RestController
public class ConfigStarter implements WebMvcConfigurer {

    @Value("${spring.config.name}")
    String name;

    @GetMapping("/name")
    public String getName() {
        return name;
    }

    /**
     * 入口方法。
     * @param args 启动参数
     */
    public static void main(String[] args) {
        // 启动应用，生成 PID 文件，取得上下文对象
        SpringApplication application = new SpringApplication(ConfigStarter.class);
        application.addListeners(new ApplicationPidFileWriter());
        application.run(args);
    }
}
