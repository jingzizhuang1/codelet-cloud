package net.codelet.cloud.error;

import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

/**
 * 请求过于频繁错误。
 */
public class TooManyRequestsError extends BaseError {

    private static final long serialVersionUID = -7451111322586134686L;

    public TooManyRequestsError() {
        this("error.too-many-requests");
    }

    public TooManyRequestsError(String errorCode, String... parameters) {
        super(TOO_MANY_REQUESTS, errorCode, parameters);
    }
}
