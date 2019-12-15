package net.codelet.cloud.error;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * 数据校验错误。
 */
public class ValidationError extends BaseError {

    private static final long serialVersionUID = 6369030003831700139L;

    @Getter
    @ApiModelProperty("发生校验错误的字段的列表")
    private List<FieldError> fields = new ArrayList<>();

    @Getter
    @ApiModelProperty("校验错误列表")
    private List<ValidationError> errors = null;

    public ValidationError() {
        this("error.validation");
    }

    public ValidationError(String errorCode) {
        super(BAD_REQUEST, errorCode);
    }

    public ValidationError(List<ValidationError> errors) {
        this();
        this.errors = errors;
    }

    public ValidationError(MethodArgumentNotValidException exception) {
        this();
        exception
            .getBindingResult()
            .getAllErrors()
            .forEach(fieldError -> fields.add((FieldError) fieldError));
    }
}
