package net.codelet.cloud.verification.vo;

/**
 * 验证码类型。
 */
public enum VerificationType {

    EMAIL("enum.verification-type.email"),
    MOBILE("enum.verification-type.mobile");

    private String text;

    VerificationType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
