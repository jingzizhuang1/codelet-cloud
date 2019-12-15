package net.codelet.cloud.starters;

import net.codelet.cloud.gateway.route.RouteLocatorBuilderWrapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Codelet Cloud 服务网关启动类。
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan({"net.codelet.cloud"})
public class SimpleGatewayStarter {

    /**
     * 入口方法。
     * @param args 应用启动参数
     */
    public static void main(String[] args) {
        // 启动应用，生成 PID 文件，取得上下文对象
        SpringApplication application = new SpringApplication(SimpleGatewayStarter.class);
        application.addListeners(new ApplicationPidFileWriter());
        application.run(args);
    }

    /**
     * 定义路由。
     * @param builder RouteLocatorBuilder
     * @return RouteLocator
     */
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return (new RouteLocatorBuilderWrapper(builder))
            // 业务参数命令服务
            .route(
                "parameter-command",
                "PUT    /parameters/{parameterName}",
                "PUT    /apps/{appId}/parameters/{parameterName}",
                "DELETE /parameters/{parameterName}",
                "DELETE /apps/{appId}/parameters/{parameterName}"
            )
            // 业务参数查询服务
            .route(
                "parameter-query",
                "GET    /parameters",
                "GET    /apps/{appId}/parameters"
            )
            // 通知命令服务
            .route(
                "notification-command",
                // 电子邮件发送配置管理接口
                "POST   /mail-configurations",
                "PATCH  /mail-configurations/{configurationId}",
                "POST   /mail-configurations/{configurationId}/disable",
                "POST   /mail-configurations/{configurationId}/enable",
                "POST   /mail-configurations/{configurationId}/set-as-default",
                "DELETE /mail-configurations/{configurationId}",
                // 短信发送配置管理接口
                "POST   /sms-configurations",
                "PATCH  /sms-configurations/{configurationId}",
                "POST   /sms-configurations/{configurationId}/disable",
                "POST   /sms-configurations/{configurationId}/enable",
                "POST   /sms-configurations/{configurationId}/set-as-default",
                "DELETE /sms-configurations/{configurationId}",
                // 通知消息模版管理接口
                "POST   /notification-templates",
                "PATCH  /notification-templates/{templateId}",
                "POST   /notification-templates/{templateId}/disable",
                "POST   /notification-templates/{templateId}/enable",
                "PUT    /notification-templates/{templateId}/languages/{languageCode}",
                "POST   /notification-templates/{templateId}/languages/{languageCode}/disable",
                "POST   /notification-templates/{templateId}/languages/{languageCode}/enable",
                "DELETE /notification-templates/{templateId}/languages/{languageCode}",
                "DELETE /notification-templates/{templateId}"
            )
            // 通知查询服务
            .route(
                "notification-query",
                // 电子邮件发送配置管理接口
                "GET    /mail-configurations",
                "GET    /mail-configurations/default",
                "GET    /mail-configurations/{configurationId}",
                // 短信发送配置管理接口
                "GET    /sms-configurations",
                "GET    /sms-configurations/default",
                "GET    /sms-configurations/{configurationId}",
                // 通知消息模版管理接口
                "GET    /notification-templates",
                "GET    /notification-templates/{templateId}",
                "GET    /notification-templates/{templateId}/languages",
                "GET    /notification-templates/{templateId}/languages/{languageCode}"
            )
            // 验证码命令服务
            .route(
                "verification-command",
                "GET    /captcha",
                "POST   /validate-captcha",
                "PUT    /verification-configurations/{keyType}/{purpose}",
                "POST   /verification-configurations/{keyType}/{purpose}/disable",
                "POST   /verification-configurations/{keyType}/{purpose}/enable",
                "DELETE /verification-configurations/{keyType}/{purpose}",
                "POST   /verifications"
            )
            // 验证码查询服务
            .route(
                "verification-query",
                "GET    /verification-configurations",
                "GET    /verification-configurations/{keyType}/{purpose}"
            )
            // 认证/授权命令服务
            .route(
                "auth-command",
                "POST   /authorizations",
                "DELETE /authorizations/{accessToken}",
                "POST   /users/{userId}/emails",
                "DELETE /users/{userId}/emails/{email}",
                "POST   /users/{userId}/mobiles",
                "DELETE /users/{userId}/mobiles/{mobile}",
                "PUT    /users/{userId}/username",
                "POST   /users/{userId}/reset-password",
                "PUT    /users/{userId}/password"
            )
            // 认证/授权查询服务
            .route(
                "auth-query",
                "GET    /credentials/{credential}"
            )
            // 用户命令服务
            .route(
                "user-command",
                "POST   /users"
            )
            // 用户查询服务
            .route(
                "user-query",
                "GET    /user",
                "GET    /users",
                "GET    /users/{userId}"
            )
            // 组织查询服务
            .route(
                "organization-query",
                "GET    /orgs/{orgId}/members"
            )
            .build();
    }
}
