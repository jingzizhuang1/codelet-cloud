package net.codelet.cloud.parameter.service;

import net.codelet.cloud.service.StringRedisService;
import net.codelet.cloud.util.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 业务参数业务逻辑。
 */
public abstract class ParameterService extends StringRedisService {

    /**
     * 取得业务参数在缓存中的 Key。
     * @param name 参数名
     * @return Redis Key
     */
    public static String redisKey(String name) {
        return redisKey(null, name);
    }

    /**
     * 取得业务参数在缓存中的 Key。
     * @param appId 应用 ID
     * @param name  参数名
     * @return Redis Key
     */
    public static String redisKey(String appId, String name) {
        if (StringUtils.isEmpty(appId)) {
            return "parameters:" + name;
        }
        return "apps:" + appId + ":parameters:" + name;
    }

    public ParameterService(StringRedisTemplate stringRedisTemplate) {
        super(stringRedisTemplate);
    }

    /**
     * 取得系统业务参数。
     * @param name 参数名
     * @return 参数值
     */
    public String getValue(String name) {
        return getValue(null, name);
    }

    /**
     * 取得应用业务参数。
     * @param appId 应用 ID
     * @param name  参数名
     * @return 参数值
     */
    public String getValue(String appId, String name) {
        return redisGet(redisKey(appId, name));
    }
}
