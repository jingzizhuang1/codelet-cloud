package net.codelet.cloud.parameter.command.api;

import net.codelet.cloud.parameter.command.dto.ParameterSetDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "${services.parameter.command.name:parameter-command}",
    contextId = "parameter-command"
)
public interface ParameterCommandApi {

    /**
     * 设置业务参数。
     */
    @PutMapping({
        "/parameters/{parameterName}",
        "/apps/{appId}/parameters/{parameterName}"
    })
    void setParameter(
        @PathVariable(value = "appId", required = false) String appId,
        @PathVariable("parameterName") String parameterName,
        @RequestParam(value = "revision", required = false) Long revision,
        @RequestBody ParameterSetDTO parameterDTO
    );

    /**
     * 删除业务参数。
     */
    @DeleteMapping({
        "/parameters/{parameterName}",
        "/apps/{appId}/parameters/{parameterName}"
    })
    void deleteParameter(
        @PathVariable(value = "appId", required = false) String appId,
        @PathVariable("parameterName") String parameterName,
        @RequestParam(value = "revision", required = false) long revision
    );
}
