package net.codelet.cloud.error;

/**
 * 访问令牌无效错误。
 */
public class AccessTokenInvalidError extends UnauthorizedError {

    private static final long serialVersionUID = 2681302667596581138L;

    public AccessTokenInvalidError() {
        super("error.access-token-invalid");
    }

    public AccessTokenInvalidError(String errorCode, String... parameters) {
        super(errorCode, parameters);
    }
}
