package net.codelet.cloud.error;

import org.springframework.http.HttpStatus;

/**
 * 操作冲突错误。
 */
public class ConflictError extends BaseError {

    private static final long serialVersionUID = 9190936007714279074L;

    public ConflictError() {
        this("error.conflict");
    }

    public ConflictError(String errorCode, String... parameters) {
        super(HttpStatus.CONFLICT, errorCode, parameters);
    }
}
