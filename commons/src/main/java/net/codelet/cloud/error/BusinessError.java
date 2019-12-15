package net.codelet.cloud.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * 业务错误。
 */
public class BusinessError extends BaseError {

    private static final long serialVersionUID = 3743279294743560666L;

    public BusinessError() {
        this("error.business");
    }

    public BusinessError(String errorCode, String... parameters) {
        super(BAD_REQUEST, errorCode, parameters);
    }
}
