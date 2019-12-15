package net.codelet.cloud.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户代理字符串工具。
 */
public class UserAgentUtils {

    private static final String USER_AGENT_VERSION = "/(\\d+(\\.\\d+)*(\\s*\\(.+?\\))?)";
    private static final Pattern USER_AGENT_PATTERN = Pattern.compile("([A-Z][a-zA-Z]*)" + USER_AGENT_VERSION);
    public static final int DIFFERENT = 0;
    public static final int IDENTICAL = 1;
    public static final int PATCH = 2;

    /**
     * 从 UserAgent 中提取应用及其版本信息。
     * @param userAgent UserAgent 字符串
     * @return 压缩后的 UserAgent 字符串
     */
    public static String compact(final String userAgent) {

        if (StringUtils.isEmpty(userAgent)) {
            return "";
        }

        Matcher matcher = USER_AGENT_PATTERN.matcher(userAgent);

        List<String> parts = new ArrayList<>();

        while (matcher.find()) {
            parts.add(matcher.group());
        }

        if (parts.size() == 0) {
            return "";
        }

        return StringUtils.first(String.join(" ", parts), 255);
    }

    /**
     * 取得 UserAgent 字符串中的应用信息。
     * @param userAgent UserAgent 字符串
     * @return 压缩后的 UserAgent 字符串
     */
    public static String client(final String userAgent) {
        return compact(userAgent).replaceAll(USER_AGENT_VERSION, "");
    }

    /**
     * 比较两个 UserAgent 字符串。
     * @param userAgent1 UserAgent 字符串1
     * @param userAgent2 UserAgent 字符串2
     * @return 比较结果
     */
    public static int compare(String userAgent1, String userAgent2) {
        if (userAgent1.equals(userAgent2)) {
            return IDENTICAL;
        }
        if (client(userAgent1).equals(client(userAgent2))) {
            return PATCH;
        }
        return DIFFERENT;
    }
}
