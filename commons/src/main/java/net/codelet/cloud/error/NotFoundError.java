package net.codelet.cloud.error;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * 资源不存在错误。
 */
public class NotFoundError extends BaseError {

    private static final long serialVersionUID = 3162591564663217324L;

    public NotFoundError() {
        this("error.not-found");
    }

    public NotFoundError(String errorCode, String... parameters) {
        super(NOT_FOUND, errorCode, parameters);
    }
}
