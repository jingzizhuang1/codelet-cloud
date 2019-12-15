package net.codelet.cloud.parameter.query.api;

import net.codelet.cloud.parameter.query.entity.ParameterQueryEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
    name = "${services.parameter.query.name:parameter-query}",
    contextId = "parameter-query"
)
public interface ParameterQueryApi {

    /**
     * 取得所有应用业务参数。
     * @param appId 应用 ID
     * @return 应用业务参数数据实体列表
     */
    @GetMapping({
        "/parameters",
        "/apps/{appId}/parameters"
    })
    List<ParameterQueryEntity> list(@PathVariable(value = "appId", required = false) String appId);

    /**
     * 取得参数值。
     * @param appId         应用 ID
     * @param parameterName 参数名
     * @return 参数值
     */
    @GetMapping({
        "/parameters/{parameterName}/value",
        "/apps/{appId}/parameters/{parameterName}/value"
    })
    String getValue(
        @PathVariable(value = "appId", required = false) String appId,
        @PathVariable("parameterName") String parameterName
    );

    /**
     * 取得参数值。
     * @param appId         应用 ID
     * @param parameterName 参数名
     * @return 参数值
     */
    @GetMapping({
        "/parameters/{parameterName}",
        "/apps/{appId}/parameters/{parameterName}"
    })
    ParameterQueryEntity get(
        @PathVariable(value = "appId", required = false) String appId,
        @PathVariable("parameterName") String parameterName
    );
}
