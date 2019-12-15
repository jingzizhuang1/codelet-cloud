package net.codelet.cloud.parameter.query.service.impl;

import net.codelet.cloud.parameter.query.entity.ParameterQueryEntity;
import net.codelet.cloud.parameter.query.repository.ParameterQueryRepository;
import net.codelet.cloud.parameter.query.service.ParameterQueryService;
import net.codelet.cloud.parameter.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务参数服务。
 */
@Component
public class ParameterQueryServiceImpl extends ParameterService implements ParameterQueryService {

    private final ParameterQueryRepository parameterQueryRepository;

    @Autowired
    public ParameterQueryServiceImpl(
        StringRedisTemplate stringRedisTemplate,
        ParameterQueryRepository parameterQueryRepository
    ) {
        super(stringRedisTemplate);
        this.parameterQueryRepository = parameterQueryRepository;
    }

    /**
     * 取得业务参数数据实体列表。
     * @param appId 应用 ID
     * @return 业务参数数据实体列表
     */
    @Override
    public List<ParameterQueryEntity> list(String appId) {
        return parameterQueryRepository.findByAppIdAndDeletedIsFalseOrderByNameAsc(appId);
    }

    /**
     * 取得业务参数值。
     * @param appId         应用 ID
     * @param parameterName 参数名
     * @return 参数值
     */
    @Override
    public String getValue(String appId, String parameterName) {
        ParameterQueryEntity parameterEntity = get(appId, parameterName);
        if (parameterEntity == null) {
            return null;
        }
        redisSet(redisKey(appId, parameterName), parameterEntity.getValue());
        return parameterEntity.getValue();
    }

    /**
     * 取得业务参数信息。
     * @param appId         应用 ID
     * @param parameterName 参数名
     * @return 参数信息
     */
    @Override
    public ParameterQueryEntity get(String appId, String parameterName) {
        return parameterQueryRepository
            .findByAppIdAndNameAndDeletedIsFalse(appId, parameterName)
            .orElse(null);
    }
}
