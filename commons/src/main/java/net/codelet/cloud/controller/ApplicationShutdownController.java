package net.codelet.cloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.util.ConsoleUtils;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = {"domain=系统", "biz=系统", "responsibility=命令"})
public class ApplicationShutdownController extends BaseController {

    private final ApplicationContext context;
    private final String applicationName;

    @Autowired
    public ApplicationShutdownController(ApplicationContext context) {
        this.context = context;
        this.applicationName = context.getEnvironment().getProperty("spring.application.name");
    }

    @ApiOperation("关闭应用（仅限管理员于内网调用）")
    @CheckUserPrivilege(administrator = true)
    @PostMapping("/shutdown")
    public void shutdown(HttpServletRequest request) {
        // TODO: check user privilege
        SpringApplication.exit(context, () -> 0);
    }

    /**
     * Spring 应用容器被销毁前输出应用关闭日志信息。
     */
    @PreDestroy
    public void onPreDestroy() {
        ConsoleUtils
            .text("\n")
            .text(String.format(" %s is shutting down... ", applicationName), true, ConsoleUtils.Color.LIGHT_GRAY, ConsoleUtils.Color.RED)
            .text("\n")
            .print(System.out);
    }

    @Bean
    public GracefulShutdown gracefulShutdown() {
        return new GracefulShutdown();
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory(final GracefulShutdown gracefulShutdown) {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(gracefulShutdown);
        return factory;
    }

    /**
     * Graceful shutdown TomcatConnectorCustomizer。
     */
    public static class GracefulShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {

        private volatile Connector connector;

        @Override
        public void customize(Connector connector) {
            this.connector = connector;
        }

        @Override
        public void onApplicationEvent(ContextClosedEvent event) {
            this.connector.pause();
            Executor executor = this.connector.getProtocolHandler().getExecutor();
            if (executor instanceof ThreadPoolExecutor) {
                try {
                    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                    threadPoolExecutor.shutdown();
                    if (!threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                        ConsoleUtils
                            .text("\n")
                            .text(" Graceful shutdown timeout. Proceeding with forceful shutdown. ", true, ConsoleUtils.Color.LIGHT_GRAY, ConsoleUtils.Color.RED)
                            .text("\n")
                            .print(System.err);
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
