package net.codelet.cloud.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.codelet.cloud.error.BaseError;
import net.codelet.cloud.error.ValidationError;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON API 错误信息。
 */
@JsonPropertyOrder({"type", "status", "code", "message", "fields", "errors"})
public class JsonApiErrorDTO extends BaseError {

    private static final long serialVersionUID = 4969444333616100354L;

    @Getter
    @ApiModelProperty("错误类型")
    private String type;

    @Getter
    @Setter
    @ApiModelProperty("发生校验错误的字段的列表")
    private List<FieldError> fields = null;

    @Getter
    @Setter
    @ApiModelProperty("错误列表")
    private List<JsonApiErrorDTO> errors = null;

    /**
     * 构造方法。
     */
    public JsonApiErrorDTO() {
    }

    /**
     * 构造方法。
     * @param exception 异常实例
     */
    public JsonApiErrorDTO(Exception exception) {
        super(exception);
        this.type = exception.getClass().getName();

        // 若为数据校验错误则设置错误字段信息
        if (exception instanceof ValidationError) {

            ValidationError validationError = (ValidationError) exception;

            List<org.springframework.validation.FieldError> fieldErrors = validationError.getFields();

            if (fieldErrors != null && fieldErrors.size() > 0) {
                fields = new ArrayList<>();
                fieldErrors.forEach(fieldError -> fields.add(new FieldError(fieldError)));
            }

            List<ValidationError> validationErrors = validationError.getErrors();

            if (validationErrors != null && validationErrors.size() > 0) {
                errors = new ArrayList<>();
                validationErrors.forEach(item -> errors.add(new JsonApiErrorDTO(item)));
            }
        }
    }

    /**
     * 构造方法。
     * @param status HTTP 状态码
     */
    public JsonApiErrorDTO(int status) {
        super(status);
    }

    /**
     * 构造方法。
     * @param status     HTTP 状态码
     * @param code       错误代码
     * @param parameters 错误消息参数
     */
    public JsonApiErrorDTO(int status, String code, String... parameters) {
        super(status, code, parameters);
    }

    private static class FieldError {

        @Getter
        @ApiModelProperty("发生错误的字段")
        private String name;

        @Getter
        @ApiModelProperty("错误类型")
        private String type;

        @Getter
        @ApiModelProperty("错误描述")
        private String message;

        public FieldError() {
            super();
        }

        FieldError(org.springframework.validation.FieldError fieldError) {
            this.name = fieldError.getField();
            this.type = fieldError.getCode();
            this.message = fieldError.getDefaultMessage();
        }
    }
}
