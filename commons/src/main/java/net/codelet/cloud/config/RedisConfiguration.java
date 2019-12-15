package net.codelet.cloud.config;

import net.codelet.cloud.util.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.time.Duration;

import static org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisPoolingClientConfigurationBuilder;

/**
 * Redis 配置。
 */
@Configuration
@PropertySource("classpath:application.yml")
public class RedisConfiguration implements Serializable {

    private static final long serialVersionUID = -38897208053106104L;

    // Redis 服务器主机名
    @Value("${spring.redis.host:}")
    private String redisHostName;

    // Redis 服务端口
    @Value("${spring.redis.port:6379}")
    private int redisPort;

    // Redis 连接密码
    @Value("${spring.redis.password:}")
    private String redisPassword;

    // Redis 数据库
    @Value("${spring.redis.database:0}")
    private int redisDatabase;

    // Redis 数据库
    @Value("${spring.redis.timeout:60000}")
    private Long redisConnectionTimeout;

    // 连接池中最小空闲数
    @Value("${spring.redis.jedis.pool.min-idle:50}")
    private int poolMinIdle;

    // 连接池中最大空闲数
    @Value("${spring.redis.jedis.pool.max-idle:300}")
    private int poolMaxIdle;

    // 连接池中最大活跃数
    @Value("${spring.redis.jedis.pool.max-active:600}")
    private int poolMaxActive;

    // 连接池连接最大等待时间
    @Value("${spring.redis.jedis.pool.max-wait:-1}")
    private long poolMaxWait;

    // 集群配置
    private RedisClusterConfigurationProperties clusterProperties;

    private RedisConnectionFactory redisConnectionFactory = null;

    /**
     * 构造方法。
     */
    @Autowired
    public RedisConfiguration(RedisClusterConfigurationProperties clusterProperties) {
        this.clusterProperties = clusterProperties;
    }

    /**
     * 属性文件配置器。
     * @return 属性文件配置器
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Redis 客户端配置（连接池配置）。
     * @return Redis 客户端配置
     */
    @Bean
    public JedisClientConfiguration getJedisClientConfiguration() {

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMinIdle(poolMinIdle);
        poolConfig.setMaxIdle(poolMaxIdle);
        poolConfig.setMaxTotal(poolMaxActive);
        poolConfig.setMaxWaitMillis(poolMaxWait);

        return ((JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder())
            .poolConfig(poolConfig)
            .and()
            .connectTimeout(Duration.ofMillis(redisConnectionTimeout))
            .build();
    }

    /**
     * Jedis 连接工厂。
     * @return Jedis 连接工厂
     */
    @Bean
    public RedisConnectionFactory connectionFactory() {

        if (redisConnectionFactory != null) {
            return redisConnectionFactory;
        }

        // 集群时
        if (clusterProperties.getNodes() != null
            && clusterProperties.getNodes().size() > 0) {

            RedisClusterConfiguration config
                = new RedisClusterConfiguration(clusterProperties.getNodes());
            config.setMaxRedirects(clusterProperties.getMaxRedirects());
            config.setPassword(RedisPassword.of(redisPassword));

            redisConnectionFactory = new JedisConnectionFactory(
                config,
                getJedisClientConfiguration()
            );

        // 单实例时
        } else if (!StringUtils.isEmpty(redisHostName)) {

            RedisStandaloneConfiguration config
                = new RedisStandaloneConfiguration(redisHostName, redisPort);
            config.setPassword(RedisPassword.of(redisPassword));
            config.setDatabase(redisDatabase);

            redisConnectionFactory = new JedisConnectionFactory(
                config,
                getJedisClientConfiguration()
            );

        // 否则创建默认的 RedisConnectionFactory 实例
        } else {
            redisConnectionFactory = new JedisConnectionFactory();
        }

        return redisConnectionFactory;
    }

    /**
     * Redis 模板。
     * @return Redis 模板
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {

        RedisConnectionFactory factory = connectionFactory();

        if (factory == null) {
            return null;
        }

        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(factory);

        return redisTemplate;
    }

    /**
     * Redis 缓存管理器。
     * @return Redis 缓存管理器
     */
    @Bean
    public RedisCacheManager cacheManager() {

        RedisConnectionFactory factory = connectionFactory();

        if (factory == null) {
            return null;
        }

        return RedisCacheManager.create(factory);
    }
}
