package net.codelet.cloud.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.ContextDTO;

import java.util.Map;

/**
 * JSON API 带结果的响应数据。
 */
public abstract class JsonApiWithDataDTO extends JsonApiDTO {

    private static final long serialVersionUID = -1310080156266223114L;

    @Getter
    @Setter
    @ApiModelProperty("相关链接")
    private Map<String, String> links = null;

    @Getter
    @Setter
    @ApiModelProperty("引用数据")
    private Map<String, Object> included = null;

    /**
     * 构造方法。
     */
    public JsonApiWithDataDTO() {
    }

    /**
     * 构造方法。
     * @param context 请求上下文
     * @param error   错误
     */
    public JsonApiWithDataDTO(ContextDTO context, Exception error) {
        super(context, error);
    }
}
