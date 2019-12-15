package net.codelet.cloud.util;

import org.apache.commons.io.IOUtils;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 数据加密工具。
 */
public class CryptoUtils {

    // 长 ID 取值的进制（0~9A~Z）
    private static final int LONG_UID_RADIX = 36;

    // 长 ID 中随机数上限（不包含）
    private static final long LONG_UID_RANDOM_MAX = 1679616;

    /**
     * 生成全局唯一的 ID（36 进制）。
     * @return 全局唯一的 ID
     */
    public static String uniqueId() {

        long milliTime = System.currentTimeMillis();
        long nanoTime = Math.abs(System.nanoTime());

        String random = "000" + Long.toString(
            Math.abs((new SecureRandom()).nextLong()) % LONG_UID_RANDOM_MAX,
            LONG_UID_RADIX
        );

        return (
            Long.toString(milliTime * 1000000 + nanoTime % 1000000, LONG_UID_RADIX)
                + random.substring(random.length() - 4)
        );
    }

    /**
     * 生成文件的 MD5 摘要。
     * @param file 文件
     * @return MD5 摘要
     */
    public static String md5(File file) {
        try {
            return md5(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            return "";
        }
    }

    /**
     * 生成输入流的 MD5 摘要。
     * @param inputStream 输入流
     * @return MD5 摘要
     */
    public static String md5(InputStream inputStream) {
        try {
            String hexString = DigestUtils.md5DigestAsHex(inputStream);
            inputStream.close();
            return hexString;
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * 生成字符串的 MD5 摘要。
     * @param string 输入字符串
     * @return MD5 摘要
     */
    public static String md5(String string) {
        return digest(string, "MD5");
    }

    /**
     * 生成字符串的 SHA-1 摘要。
     * @param string 输入字符串
     * @return SHA-1 摘要
     */
    public static String sha(String string) {
        return digest(string, "SHA-1");
    }

    /**
     * 生成字符串的 SHA-256 摘要。
     * @param string 输入字符串
     * @return SHA-256 摘要
     */
    public static String sha256(String string) {
        return digest(string, "SHA-256");
    }

    /**
     * 生成字符串的 SHA-384 摘要。
     * @param string 输入字符串
     * @return SHA-384 摘要
     */
    public static String sha354(String string) {
        return digest(string, "SHA-384");
    }

    /**
     * 生成字符串的 SHA-512 摘要。
     * @param string 输入字符串
     * @return SHA-512 摘要
     */
    public static String sha512(String string) {
        return digest(string, "SHA-512");
    }

    /**
     * 计算字符串摘要。
     * @param string 字符串
     * @param algorithm 摘要算法
     * @return 字符串摘要
     */
    private static String digest(String string, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(string == null ? (new byte[] {}) : string.getBytes());
            return StringUtils.toHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /**
     * 生成字符串的 Base64 编码。
     * @param string 编码字符串
     * @return Base64 字符串
     */
    public static String encodeBase64(String string) {
        return encodeBase64(string.getBytes());
    }

    /**
     * 生成 Base64 编码。
     * @param inputStream 输入流
     * @return Base64 字符串
     */
    public static String encodeBase64(
        InputStream inputStream
    ) throws IOException {
        return encodeBase64(IOUtils.toByteArray(inputStream));
    }

    /**
     * 生成 Base64 编码。
     * @param bytes 字节数组
     * @return Base64 字符串
     */
    public static String encodeBase64(byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes));
    }

    /**
     * 解码 Base64 字符串。
     * @param string Base64 字符串
     * @return 解码后的字符串
     */
    public static String decodeBase64(String string) {
        return new String(Base64.getDecoder().decode(string.getBytes()));
    }

}
