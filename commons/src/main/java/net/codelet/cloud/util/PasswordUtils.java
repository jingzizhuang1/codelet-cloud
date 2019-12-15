package net.codelet.cloud.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtils {

    /**
     * 使用 BCrypt 算法对密码加密。
     * @param password 原始密码
     * @param rounds   迭代次数
     * @return 加密后的密码
     */
    public static String encryptPassword(String password, int rounds) {
        return BCrypt.hashpw(CryptoUtils.md5(password), BCrypt.gensalt(rounds));
    }

    /**
     * 校验密码。
     * @param password          密码
     * @param passwordEncrypted 密码密文
     * @return 密码是否相符
     */
    public static boolean validatePassword(
        final String password,
        final String passwordEncrypted
    ) {
        return BCrypt.checkpw(CryptoUtils.md5(password), passwordEncrypted);
    }

    /**
     * 计算密码强度。
     * @param password 密码
     * @return 密码强度
     */
    public static Integer passwordStrength(String password) {
        // TODO: calculate password strength
        return 0;
    }

}
