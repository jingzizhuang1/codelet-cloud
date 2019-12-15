package net.codelet.cloud.error;

import org.springframework.http.HttpStatus;

/**
 * 访问拒绝。
 */
public class AccessDeniedError extends BaseError {

    private static final long serialVersionUID = 5012253355903728775L;

    public AccessDeniedError() {
        this("error.access-denied");
    }

    public AccessDeniedError(String errorCode, String... parameters) {
        super(HttpStatus.FORBIDDEN, errorCode, parameters);
    }
}
