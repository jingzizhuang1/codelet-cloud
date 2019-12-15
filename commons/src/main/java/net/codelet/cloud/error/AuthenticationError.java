package net.codelet.cloud.error;

/**
 * 认证失败错误。
 */
public class AuthenticationError extends UnauthorizedError {

    private static final long serialVersionUID = -3319441007439434768L;

    public AuthenticationError() {
        this("error.invalid-credentials");
    }

    public AuthenticationError(String errorCode, String... parameters) {
        super(errorCode, parameters);
    }
}
