package net.codelet.cloud.vo;

/**
 * 性别。
 */
public enum Gender {

    FEMALE("enum.gender.female"),
    MALE("enum.gender.female");

    private String text;

    Gender(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
