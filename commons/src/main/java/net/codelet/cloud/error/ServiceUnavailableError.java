package net.codelet.cloud.error;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

/**
 * 服务不可用错误。
 */
public class ServiceUnavailableError extends BaseError {

    private static final long serialVersionUID = 7480143797493354253L;

    public ServiceUnavailableError() {
        this("error.service-unavailable");
    }

    public ServiceUnavailableError(String errorCode, String... parameters) {
        super(SERVICE_UNAVAILABLE, errorCode, parameters);
    }
}
