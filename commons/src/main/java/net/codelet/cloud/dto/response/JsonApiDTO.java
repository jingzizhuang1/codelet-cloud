package net.codelet.cloud.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.dto.ContextDTO;
import net.codelet.cloud.error.BaseError;

import javax.servlet.http.HttpServletResponse;

/**
 * JSON API 响应数据。
 */
@JsonPropertyOrder({"success", "error"})
public class JsonApiDTO extends BaseDTO {

    private static final long serialVersionUID = -4230078514620380503L;

    @Getter @Setter
    @ApiModelProperty("请求是否被成功处理")
    private Boolean success = true;

    @Getter @Setter
    @ApiModelProperty("错误信息")
    private JsonApiErrorDTO error = null;

    @Getter @Setter
    @JsonIgnore
    private ContextDTO context = null;

    /**
     * 构造方法。
     */
    public JsonApiDTO() {
    }

    /**
     * 构造方法。
     * @param context 请求上下文
     */
    public JsonApiDTO(ContextDTO context) {
        this.setContext(context);
    }

    /**
     * 构造方法。
     * @param context 请求上下文
     * @param error   错误
     */
    public JsonApiDTO(ContextDTO context, Exception error) {
        this(context);
        this.setSuccess(false);

        HttpServletResponse response = context.getResponse();
        int statusCode;

        if (error instanceof JsonApiErrorDTO) {
            JsonApiErrorDTO errorDTO = (JsonApiErrorDTO) error;
            this.setError(errorDTO);
            statusCode = errorDTO.getStatus();
        } else {
            this.setError(new JsonApiErrorDTO(error));
            if (error instanceof BaseError) {
                statusCode = ((BaseError) error).getStatus();
            } else {
                statusCode = 500;
            }
        }

        response.setStatus(statusCode);
    }
}
