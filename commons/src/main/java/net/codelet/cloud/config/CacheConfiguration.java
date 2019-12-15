package net.codelet.cloud.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.Serializable;

/**
 * 应用缓存配置。
 */
@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "application.cache")
public class CacheConfiguration implements Serializable {

    private static final long serialVersionUID = 6365823943777696417L;

    // 用户令牌最后刷新时间（Unix 纪元时间，秒）
    @Getter
    @Setter
    @Value("${ttl.access-token-renewed-at:30}")
    private int accessTokenRenewedAtTTL;
}
