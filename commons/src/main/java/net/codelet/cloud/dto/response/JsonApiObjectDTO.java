package net.codelet.cloud.dto.response;

import net.codelet.cloud.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.ContextDTO;

/**
 * JSON API 对象响应数据。
 * @param <T> 返回结果的类型
 */
@JsonPropertyOrder({"success", "links", "data", "error", "included"})
public class JsonApiObjectDTO<T extends BaseDTO> extends JsonApiWithDataDTO {

    private static final long serialVersionUID = -4468893780959116855L;

    @Getter @Setter
    @ApiModelProperty("响应数据")
    private T data = null;

    /**
     * 构造方法。
     */
    public JsonApiObjectDTO() {
    }

    /**
     * 构造方法。
     * @param data 返回结果
     */
    public JsonApiObjectDTO(T data) {
        this.setData(data);
    }

    /**
     * 构造方法。
     * @param context 请求上下文
     * @param error   错误
     */
    public JsonApiObjectDTO(ContextDTO context, Exception error) {
        super(context, error);
    }
}
