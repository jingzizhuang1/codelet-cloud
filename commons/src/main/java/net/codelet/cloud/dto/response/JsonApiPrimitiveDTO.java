package net.codelet.cloud.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.ContextDTO;

/**
 * JSON API 对象响应数据。
 */
@JsonPropertyOrder({"success", "data", "error"})
public class JsonApiPrimitiveDTO<T> extends JsonApiDTO {

    private static final long serialVersionUID = 8681800899734159294L;

    @Getter
    @Setter
    @ApiModelProperty("响应数据")
    private T data = null;

    /**
     * 构造方法。
     */
    public JsonApiPrimitiveDTO() {
    }

    /**
     * 构造方法。
     * @param data 返回结果
     */
    public JsonApiPrimitiveDTO(T data) {
        this.setData(data);
    }

    /**
     * 构造方法。
     * @param context 请求上下文
     * @param error   错误
     */
    public JsonApiPrimitiveDTO(ContextDTO context, Exception error) {
        super(context, error);
    }
}
