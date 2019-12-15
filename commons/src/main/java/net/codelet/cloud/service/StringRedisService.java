package net.codelet.cloud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.codelet.cloud.util.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Redis 服务基类。
 */
public abstract class StringRedisService {

    private static final ObjectMapper objectMapper = (new ObjectMapper())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    // Redis 模板
    private final StringRedisTemplate stringRedisTemplate;
    private final ValueOperations<String, String> redisValueOperations;

    /**
     * 构造方法。
     * @param stringRedisTemplate Redis 模板
     */
    public StringRedisService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisValueOperations = stringRedisTemplate.opsForValue();
    }

    /**
     * 缓存值。
     * @param key   KEY
     * @param value 值
     */
    protected void redisSet(String key, String value) {
        redisValueOperations.set(key, value);
    }

    /**
     * 缓存值。
     * @param key     KEY
     * @param value   值
     * @param seconds TTL 秒
     */
    protected void redisSet(String key, String value, int seconds) {
        redisValueOperations.set(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 缓存对象。
     * @param key   KEY
     * @param value 对象
     */
    protected void redisSetObject(String key, Object value) {
        try {
            redisSet(key, objectMapper.writeValueAsString(value));
        } catch (JsonProcessingException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * 缓存对象。
     * @param key     KEY
     * @param value   对象
     * @param seconds TTL 秒
     */
    protected void redisSetObject(String key, Object value, int seconds) {
        try {
            redisSet(key, objectMapper.writeValueAsString(value), seconds);
        } catch (JsonProcessingException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * 取得缓存的值。
     * @param key KEY
     * @return 值
     */
    protected String redisGet(String key) {
        return redisValueOperations.get(key);
    }

    /**
     * 取得缓存的值。
     * @param key     KEY
     * @param seconds 延长 TTL 秒
     * @return 值
     */
    protected String redisGet(String key, int seconds) {
        String value = redisGet(key);
        if (value != null) {
            stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        }
        return value;
    }

    /**
     * 取得缓存的对象。
     * @param key  KEY
     * @param type 缓存的对象的类型
     * @param <T>  缓存的对象的范型
     * @return 缓存的对象
     */
    protected <T> T redisGetObject(String key, Class<T> type) {
        return redisGetObject(key, type, null);
    }

    /**
     * 取得缓存的对象。
     * @param key     KEY
     * @param type    缓存的对象的类型
     * @param seconds TTL 秒
     * @param <T>     缓存的对象的范型
     * @return 缓存的对象
     */
    protected <T> T redisGetObject(String key, Class<T> type, Integer seconds) {
        String json = seconds == null ? redisGet(key) : redisGet(key, seconds);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * 删除缓存的值。
     * @param key KEY
     */
    protected void redisDelete(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 判断是否存在缓存的值。
     * @param key KEY
     * @return 是否存在
     */
    protected Boolean redisHas(String key) {
        return stringRedisTemplate.hasKey(key);
    }
}
