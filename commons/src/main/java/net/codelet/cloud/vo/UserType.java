package net.codelet.cloud.vo;

import lombok.Getter;

/**
 * 用户账号类型。
 */
public enum UserType {

    SYSTEM("enum.user-type.system", true),
    SUPER("enum.user-type.super", true),
    ADMINISTRATOR("enum.user-type.administrator", true),
    USER("enum.user-type.user", false);

    @Getter
    private String text;

    @Getter
    private boolean isAdministrator;

    UserType(String text, boolean isAdministrator) {
        this.text = text;
        this.isAdministrator = isAdministrator;
    }

    public static UserType getByName(String name) {
        for (UserType userType : UserType.values()) {
            if (userType.name().equals(name)) {
                return userType;
            }
        }
        return null;
    }
}
