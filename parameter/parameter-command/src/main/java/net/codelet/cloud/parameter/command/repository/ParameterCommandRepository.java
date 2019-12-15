package net.codelet.cloud.parameter.command.repository;

import net.codelet.cloud.repository.BaseRepository;
import net.codelet.cloud.parameter.command.entity.ParameterCommandEntity;

import java.util.Optional;

/**
 * 业务参数数据仓库。
 */
public interface ParameterCommandRepository extends BaseRepository<ParameterCommandEntity> {

    /**
     * 取得业务参数。
     * @param appId 应用 ID
     * @param name  参数名
     * @return 参数信息
     */
    Optional<ParameterCommandEntity> findByAppIdAndName(String appId, String name);
}
