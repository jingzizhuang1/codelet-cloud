package net.codelet.cloud.error;

/**
 * 数据重复错误。
 */
public class DuplicatedError extends ConflictError {

    private static final long serialVersionUID = -1182594666355297225L;

    public DuplicatedError() {
        this("error.duplicated");
    }

    public DuplicatedError(String code, String... parameters) {
        super(code, parameters);
    }
}
