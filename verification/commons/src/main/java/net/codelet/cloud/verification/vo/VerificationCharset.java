package net.codelet.cloud.verification.vo;

import net.codelet.cloud.util.RandomUtils;

/**
 * 验证码字符集。
 */
public enum VerificationCharset {
    NUMBER("0123456789"),
    HEXADECIMAL("0123456789ABCDEF"),
    ALPHABET_NUMBER("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");

    private final String charset;

    VerificationCharset(String charset) {
        this.charset = charset;
    }

    /**
     * 生成验证码。
     * @param length 验证码长度
     * @return 验证码
     */
    public String generate(int length) {
        return RandomUtils.text(charset, length);
    }
}
