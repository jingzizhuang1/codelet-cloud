package net.codelet.cloud.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 输入/输出实用工具。
 */
public class IOUtils {

    // 默认字符集：UTF-8
    private static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.name();

    /**
     * 将输入流作为字符串读取。
     * @param is 输入流
     * @return 输入流的字符串内容
     */
    public static String readAsString(InputStream is) {
        StringWriter writer = new StringWriter();
        try {
            org.apache.commons.io.IOUtils.copy(is, writer, DEFAULT_CHARSET);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return null;
        }
        return writer.toString();
    }
}
