package net.codelet.cloud.starters.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;

/**
 * Swagger2 配置类。
 */
@Configuration
@EnableSwagger2
@PropertySource("classpath:swagger.yml")
public class Swagger2Configuration implements WebMvcConfigurer {

    @Value("${CC_SWAGGER_UI_DIR:}")
    private String swaggerUiDir;

    /**
     * 创建 REST 接口说明文档。
     * @return API 文档创建器
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            //.ignoredParameterTypes(ContextDTO.class)
            .apiInfo(
                new ApiInfoBuilder()
                    .title("Codelet Cloud")
                    .description("REST 接口说明文档。")
                    .version("0.0.1-SNAPSHOT")
                    .build()
            )
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
            .paths(PathSelectors.any())
            .build();
    }

    /**
     * 注册视图控制器。
     * @param registry 注册器
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/docs").setViewName("redirect:/docs/");
        registry.addViewController("/docs/").setViewName("forward:/docs/index.html");
    }

    /**
     * 注册静态资源。
     * @param registry 注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (StringUtils.isEmpty(swaggerUiDir) || !(new File(swaggerUiDir)).exists()) {
            registry.addResourceHandler("/docs/**").addResourceLocations("classpath:/public/docs/");
        } else {
            registry.addResourceHandler("/docs/**").addResourceLocations("file:" + swaggerUiDir);
        }
    }
}
