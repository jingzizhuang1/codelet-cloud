package net.codelet.cloud.auth.vo;

import lombok.Getter;
import net.codelet.cloud.util.RegExpUtils;

/**
 * 认证凭证类型。
 */
public enum CredentialType {

    USERNAME("enum.credential-type.username", RegExpUtils.USERNAME),
    EMAIL("enum.credential-type.email", RegExpUtils.EMAIL),
    MOBILE("enum.credential-type.mobile", RegExpUtils.MOBILE);

    @Getter
    private String text;

    @Getter
    private String pattern;

    CredentialType(String text, String pattern) {
        this.text = text;
        this.pattern = pattern;
    }
}
