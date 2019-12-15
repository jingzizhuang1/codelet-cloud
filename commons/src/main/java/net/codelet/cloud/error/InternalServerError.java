package net.codelet.cloud.error;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * 服务器内部错误。
 */
public class InternalServerError extends BaseError {

    private static final long serialVersionUID = 662805228750418574L;

    public InternalServerError() {
        this("error.internal-server-error");
    }

    public InternalServerError(String errorCode, String... parameters) {
        super(INTERNAL_SERVER_ERROR, errorCode, parameters);
    }
}
