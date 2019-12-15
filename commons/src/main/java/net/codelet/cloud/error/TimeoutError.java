package net.codelet.cloud.error;

import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;

/**
 * 请求超时错误。
 */
public class TimeoutError extends BaseError {

    private static final long serialVersionUID = -5954658070774235849L;

    public TimeoutError() {
        this("error.timeout");
    }

    public TimeoutError(String errorCode, String... parameters) {
        super(REQUEST_TIMEOUT, errorCode, parameters);
    }
}
