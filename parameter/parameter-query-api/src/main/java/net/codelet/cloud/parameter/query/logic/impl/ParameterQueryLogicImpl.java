package net.codelet.cloud.parameter.query.logic.impl;

import net.codelet.cloud.parameter.query.api.ParameterQueryApi;
import net.codelet.cloud.parameter.query.logic.ParameterQueryLogic;
import net.codelet.cloud.parameter.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 业务参数查询业务逻辑。
 */
@Component
public class ParameterQueryLogicImpl extends ParameterService implements ParameterQueryLogic {

    private final ParameterQueryApi parameterQueryApi;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ParameterQueryLogicImpl(
        StringRedisTemplate stringRedisTemplate,
        ParameterQueryApi parameterQueryApi
    ) {
        super(stringRedisTemplate);
        this.parameterQueryApi = parameterQueryApi;
    }

    /**
     * 取得业务参数值。
     * @param parameterName 参数名
     * @return 参数值
     */
    @Override
    public String getValue(String parameterName) {
        return getValue(null, parameterName);
    }

    /**
     * 取得业务参数值。
     * @param appId         应用 ID
     * @param parameterName 参数名
     * @return 参数值
     */
    @Override
    public String getValue(String appId, String parameterName) {
        String value = super.getValue(appId, parameterName);
        if (value != null) {
            return value;
        }
        return parameterQueryApi.getValue(appId, parameterName);
    }
}
