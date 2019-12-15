package net.codelet.cloud.error;

import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

/**
 * 媒体类型不被支持错误。
 */
public class UnsupportedMediaTypeError extends BaseError {

    private static final long serialVersionUID = 3039684466257739351L;

    public UnsupportedMediaTypeError() {
        this("error.unsupported-mime-type");
    }

    public UnsupportedMediaTypeError(String errorCode, String... parameters) {
        super(UNSUPPORTED_MEDIA_TYPE, errorCode, parameters);
    }
}
