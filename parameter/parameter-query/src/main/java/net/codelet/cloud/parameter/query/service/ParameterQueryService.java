package net.codelet.cloud.parameter.query.service;

import net.codelet.cloud.parameter.query.entity.ParameterQueryEntity;

import java.util.List;

/**
 * 业务参数服务接口。
 */
public interface ParameterQueryService {

    /**
     * 取得业务参数数据实体列表。
     * @param appId 应用 ID
     * @return 业务参数数据实体列表
     */
    List<ParameterQueryEntity> list(String appId);

    /**
     * 取得业务参数值。
     * @param appId         应用 ID
     * @param parameterName 参数名
     * @return 参数值
     */
    String getValue(String appId, String parameterName);

    /**
     * 取得业务参数信息。
     * @param appId         应用 ID
     * @param parameterName 参数名
     * @return 参数信息
     */
    ParameterQueryEntity get(String appId, String parameterName);
}
