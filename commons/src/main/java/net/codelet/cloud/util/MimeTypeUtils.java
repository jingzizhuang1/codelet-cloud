package net.codelet.cloud.util;

/**
 * MIME 类型处理工具。
 */
public class MimeTypeUtils {

    /**
     * 检查 MIME 类型是否与限定的类型匹配。
     * @param mimeTypePatterns 限定的类型
     * @param mimeType         MIME 类型
     * @return 是否匹配
     */
    public static boolean match(String[] mimeTypePatterns, String mimeType) {
        for (String mimeTypePattern : mimeTypePatterns) {
            if (match(mimeTypePattern, mimeType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查 MIME 类型是否与限定的类型匹配。
     * @param mimeTypePattern 限定的类型
     * @param mimeType        MIME 类型
     * @return 是否匹配
     */
    public static boolean match(String mimeTypePattern, String mimeType) {

        if (mimeType == null || !mimeType.matches("^[^*/]+/[^*/]+(;.+)?$")) {
            return false;
        }

        mimeType = mimeType.split(";")[0];

        if (StringUtils.isEmpty(mimeTypePattern)
            || mimeTypePattern.equals("*/*")
            || mimeType.equals(mimeTypePattern)) {
            return true;
        }

        String[] pattern = mimeTypePattern.split("/");
        String[] type = mimeType.split("/");

        // 主类型不可使用通配符
        if (pattern[0].equals(type[0])) {
            // 子类型可以使用通配符
            if (pattern[1].equals("*") || pattern[1].equals(type[1])) {
                return true;
            }
            // 兼容一些服务返回的不标准的 MIME 类型（如将 image/jpeg 记为 image/jpg）
            if (pattern[0].equals("image") && pattern[1].equals("jpeg") && type[1].equals("jpg")) {
                return true;
            }
        }

        return false;
    }

}
