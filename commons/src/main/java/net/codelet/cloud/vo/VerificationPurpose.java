package net.codelet.cloud.vo;

import lombok.Getter;

/**
 * 验证目的。
 */
public enum VerificationPurpose {

    USER_SIGN_UP("enum.verification-purpose.user-sign-up"),
    USER_SIGN_IN("enum.verification-purpose.user-sign-in"),
    RESET_PASSWORD("enum.verification-purpose.reset-password");

    @Getter
    private String text;

    VerificationPurpose(String text) {
        this.text = text;
    }
}
