package net.codelet.cloud.error;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * 未授权错误。
 */
public class UnauthorizedError extends BaseError {

    private static final long serialVersionUID = 52593004496429873L;

    public UnauthorizedError() {
        this("error.unauthorized");
    }

    public UnauthorizedError(String errorCode, String... parameters) {
        super(UNAUTHORIZED, errorCode, parameters);
    }
}
