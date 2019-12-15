package net.codelet.cloud.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Redis 集群配置属性。
 */
@Component
@ConfigurationProperties("spring.redis.cluster")
public class RedisClusterConfigurationProperties {

    // 节点列表
    @Getter @Setter
    private List<String> nodes;

    // 最大重定向数
    @Getter @Setter
    private Integer maxRedirects;
}
