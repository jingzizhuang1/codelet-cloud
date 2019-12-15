package net.codelet.cloud.parameter.command.service.impl;

import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.error.ConflictError;
import net.codelet.cloud.parameter.command.dto.ParameterSetDTO;
import net.codelet.cloud.parameter.command.entity.ParameterCommandEntity;
import net.codelet.cloud.parameter.command.repository.ParameterCommandRepository;
import net.codelet.cloud.parameter.command.service.ParameterCommandService;
import net.codelet.cloud.parameter.service.ParameterService;
import net.codelet.cloud.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 业务参数服务。
 */
@Component
public class ParameterCommandServiceImpl extends ParameterService implements ParameterCommandService {

    private final ParameterCommandRepository parameterCommandRepository;

    @Autowired
    public ParameterCommandServiceImpl(
        StringRedisTemplate stringRedisTemplate,
        ParameterCommandRepository parameterCommandRepository
    ) {
        super(stringRedisTemplate);
        this.parameterCommandRepository = parameterCommandRepository;
    }

    /**
     * 从数据库取得业务参数数据。
     * @param appId         应用 ID
     * @param parameterName 参数名
     * @param revision      修订版本号
     * @return 业务参数数据实体
     */
    private ParameterCommandEntity get(String appId, String parameterName, Long revision) {
        ParameterCommandEntity parameterEntity = parameterCommandRepository
            .findByAppIdAndName(appId, parameterName).orElse(null);
        if ((parameterEntity != null && !parameterEntity.getRevision().equals(revision))
            || (parameterEntity == null && revision != null)) {
            throw new ConflictError();
        }
        return parameterEntity;
    }

    /**
     * 设置业务参数。
     * @param operator      操作者
     * @param appId         应用 ID
     * @param parameterName 参数名
     * @param revision      修订版本号
     * @param parameterDTO  参数信息数据
     */
    @Override
    public void set(
        OperatorDTO operator,
        String appId,
        String parameterName,
        Long revision,
        ParameterSetDTO parameterDTO
    ) {
        ParameterCommandEntity parameterEntity = get(appId, parameterName, revision);
        Date timestamp = new Date();
        if (parameterEntity == null) {
            parameterEntity = new ParameterCommandEntity();
            parameterEntity.setAppId(appId);
            parameterEntity.setName(parameterName);
            parameterEntity.setCreatedAt(timestamp);
            parameterEntity.setCreatedBy(operator.getId());
        }
        if (!parameterEntity.getDeleted()
            && !BeanUtils.merge(parameterDTO, parameterEntity)) {
            return;
        }
        parameterEntity.setLastModifiedAt(timestamp);
        parameterEntity.setLastModifiedBy(operator.getId());
        parameterEntity.setDeleted(false);
        parameterEntity.updateRevision();
        parameterCommandRepository.save(parameterEntity);
        redisSet(redisKey(appId, parameterName), parameterEntity.getValue());
    }

    /**
     * 删除业务参数。
     * @param operator      操作者
     * @param appId         应用 ID
     * @param parameterName 参数名
     * @param revision      修订版本号
     */
    @Override
    public void delete(
        OperatorDTO operator,
        String appId,
        String parameterName,
        Long revision
    ) {
        redisDelete(redisKey(appId, parameterName));
        ParameterCommandEntity parameterEntity = get(appId, parameterName, revision);
        parameterEntity.setDeleted(true);
        parameterEntity.setDeletedAt(new Date());
        parameterEntity.setDeletedBy(operator.getId());
        parameterEntity.updateRevision();
        parameterCommandRepository.save(parameterEntity);
    }
}
