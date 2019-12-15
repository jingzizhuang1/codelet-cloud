package net.codelet.cloud.error;

import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;

/**
 * 请求数据过大错误。
 */
public class PayloadTooLargeError extends BaseError {

    private static final long serialVersionUID = -1883645509061082968L;

    public PayloadTooLargeError() {
        this("error.payload-too-large");
    }

    public PayloadTooLargeError(String errorCode, String... parameters) {
        super(PAYLOAD_TOO_LARGE, errorCode, parameters);
    }
}
