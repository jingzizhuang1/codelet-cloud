package net.codelet.cloud.error;

/**
 * 访问令牌过期错误。
 */
public class AccessTokenExpiredError extends UnauthorizedError {

    private static final long serialVersionUID = 8923970833337031897L;

    public AccessTokenExpiredError() {
        super("error.access-token-expired");
    }

    public AccessTokenExpiredError(String errorCode, String... parameters) {
        super(errorCode, parameters);
    }
}
