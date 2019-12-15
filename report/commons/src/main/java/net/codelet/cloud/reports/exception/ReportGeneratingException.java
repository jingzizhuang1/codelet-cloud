package net.codelet.cloud.reports.exception;

import java.io.Serializable;

/**
 * 报表生成异常
 */
public class ReportGeneratingException extends Exception implements Serializable {

    private static final long serialVersionUID = 6516779139621958850L;

    /**
     * 异常信息
     *
     * @param message 异常信息
     */
    public ReportGeneratingException(String message) {
        super(message);
    }

    /**
     * 异常信息 + 异常原因
     *
     * @param message 异常信息
     * @param cause 异常原因
     */
    public ReportGeneratingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 异常原因
     *
     * @param cause 异常原因
     */
    public ReportGeneratingException(Throwable cause) {
        super(cause);
    }
}
