package net.codelet.cloud.parameter.command.service;

import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.parameter.command.dto.ParameterSetDTO;

/**
 * 业务参数服务接口。
 */
public interface ParameterCommandService {

    /**
     * 设置业务参数。
     * @param operator      操作者
     * @param appId         应用 ID
     * @param parameterName 参数名
     * @param revision      修订版本号
     * @param parameterDTO  参数信息数据
     */
    void set(
        OperatorDTO operator,
        String appId,
        String parameterName,
        Long revision,
        ParameterSetDTO parameterDTO
    );

    /**
     * 删除业务参数。
     * @param operator      操作者
     * @param appId         应用 ID
     * @param parameterName 参数名
     * @param revision      修订版本号
     */
    void delete(
        OperatorDTO operator,
        String appId,
        String parameterName,
        Long revision
    );
}
