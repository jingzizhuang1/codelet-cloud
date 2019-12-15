package net.codelet.cloud.vo;

import lombok.Getter;

/**
 * 主体类型。
 */
public enum PrincipalType {

    USER("enum.principal.user"),
    ORGANIZATION("enum.principal.organization");

    @Getter
    private String text;

    PrincipalType(String text) {
        this.text = text;
    }

    public static PrincipalType getByName(String name) {
        for (PrincipalType principalType : PrincipalType.values()) {
            if (principalType.name().equals(name)) {
                return principalType;
            }
        }
        return null;
    }
}
