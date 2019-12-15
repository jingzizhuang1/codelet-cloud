package net.codelet.cloud.parameter.query.repository;

import net.codelet.cloud.repository.BaseRepository;
import net.codelet.cloud.parameter.query.entity.ParameterQueryEntity;

import java.util.List;
import java.util.Optional;

/**
 * 业务参数数据仓库。
 */
public interface ParameterQueryRepository extends BaseRepository<ParameterQueryEntity> {

    /**
     * 取得业务参数数据实体列表。
     * @param appId 应用 ID
     * @return 业务参数数据实体列表
     */
    List<ParameterQueryEntity> findByAppIdAndDeletedIsFalseOrderByNameAsc(String appId);

    /**
     * 取得业务参数数据。
     * @param appId 应用 ID
     * @param name  参数名
     * @return 参数数据
     */
    Optional<ParameterQueryEntity> findByAppIdAndNameAndDeletedIsFalse(String appId, String name);
}
