package net.codelet.cloud.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.util.I18nUtils;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * 业务异常基类。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseError extends RuntimeException {

    private static final long serialVersionUID = 2177612828588673257L;

    @Getter
    @ApiModelProperty("错误代码")
    private String code;

    @Getter
    @ApiModelProperty("错误消息")
    private String message;

    @Getter
    @ApiModelProperty("HTTP 状态码")
    private int status;

    @Getter
    @Setter
    @JsonIgnore
    @ApiModelProperty(value = "返回数据", hidden = true)
    private Object data;

    /**
     * 构造方法。
     */
    public BaseError() {
    }

    /**
     * 构造方法。
     * @param code       错误代码
     * @param parameters 消息参数
     */
    public BaseError(String code, String... parameters) {
        this(INTERNAL_SERVER_ERROR, code, parameters);
    }

    /**
     * 构造方法。
     * @param status     HTTP 状态码
     * @param code       错误代码
     * @param parameters 消息参数
     */
    public BaseError(HttpStatus status, String code, String... parameters) {
        this(status.value(), code, parameters);
    }

    /**
     * 构造方法。
     * @param status HTTP 状态码
     */
    public BaseError(int status) {
        this.status = status;
        try {
            this.code = HttpStatus.valueOf(status).name();
            this.message = this.code;
        } catch (IllegalArgumentException ignored) {
        }
    }

    /**
     * 构造方法。
     * @param status     HTTP 状态码
     * @param code       错误代码
     * @param parameters 消息参数
     */
    public BaseError(int status, String code, String... parameters) {
        this();
        this.code = code;
        this.status = status;
        setMessageSource(parameters);
    }

    /**
     * 构造方法。
     * @param exception  异常
     * @param parameters 消息参数
     */
    public BaseError(Exception exception, String... parameters) {
        this();

        code = "error";
        status = INTERNAL_SERVER_ERROR.value();

        if (exception instanceof BaseError) {
            BaseError error = (BaseError) exception;
            code = error.getCode();
            status = error.getStatus();
            message = error.getMessage();
        } else if (exception instanceof CannotAcquireLockException) {
            code = "error.db.lock-acquisition";
        } else if (exception != null) {
            message = exception.getMessage();
        }

        setMessageSource(parameters);
    }

    /**
     * 设置消息源，更新消息内容。
     * @param parameters 消息参数
     */
    private void setMessageSource(String... parameters) {
        setMessage(I18nUtils.message(code, parameters));
    }

    /**
     * 设置错误消息。
     * @param message 错误消息
     */
    @JsonSetter
    public void setMessage(String message) {
        this.message = message;
    }
}
