package net.codelet.cloud.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具。
 */
public class RegExpUtils {

    // 数据实体 ID 格式
    public static final String ID = "^[0-9A-Z]{16}$";
    public static final String GUID = "^[0-9a-fA-F]+(-[0-9a-fA-F]+)+$";

    // 登录用户名格式
    public static final String USERNAME = "^[a-z](_?[0-9a-z]{2,}){1,31}$";

    // 手机号码格式
    public static final String MOBILE = "^(\\+?0?86)?((1[345789][0-9])([0-9]{4})([0-9]{4}))$";
    private static final Pattern MOBILE_PATTERN = Pattern.compile(MOBILE);

    // 电子邮箱地址格式
    public static final String EMAIL =  "^[0-9a-zA-Z]+([._%+\\-][0-9a-zA-Z]+)*@[0-9a-zA-Z]+([.\\-][0-9a-zA-Z]+)*\\.[a-zA-Z]{2,6}$";

    // 权限值
    public static final String PRIVILEGE = "^[0-9A-Z]+([_/][0-9A-Z]+)*$";

    /**
     * 检查输入的值是否符合数据实体 ID 格式。
     * @param string 输入值
     * @return 是否符合数据实体 ID 格式
     */
    public static boolean isID(String string) {
        return string != null && Pattern.matches(ID, string);
    }

    /**
     * 检查输入的值是否符合 GUID 格式。
     * @param string 输入值
     * @return 是否符合 GUID 格式
     */
    public static boolean isGUID(String string) {
        return string != null && Pattern.matches(GUID, string);
    }

    /**
     * 检查输入的值是否符合登录用户名格式。
     * @param string 输入值
     * @return 是否符合登录用户名格式
     */
    public static boolean isUsername(String string) {
        return string != null && Pattern.matches(USERNAME, string);
    }

    /**
     * 检查输入的值是否符合手机号码格式。
     * @param string 输入值
     * @return 是否符合手机号码格式
     */
    public static boolean isMobileNo(String string) {
        return string != null && Pattern.matches(MOBILE, string);
    }

    /**
     * 隐藏手机号码部分数字。
     * @param mobileNo 手机号码
     * @return 更新后的手机号码
     */
    public static String fakeMobileNo(String mobileNo) {
        Matcher matcher = MOBILE_PATTERN.matcher(mobileNo);
        if (!matcher.matches()) {
            return null;
        }
        return matcher.group(3) + "****" + matcher.group(5);
    }

    /**
     * 检查输入的值是否符合电子邮箱地址格式。
     * @param string 输入值
     * @return 是否符合电子邮箱地址格式
     */
    public static boolean isEmailAddress(String string) {
        return string != null && Pattern.matches(EMAIL, string);
    }

    /**
     * 检查输入的值是否符合权限格式。
     * @param string 输入值
     * @return 是否符合权限格式
     */
    public static boolean isPrivilege(String string) {
        return string != null && Pattern.matches(PRIVILEGE, string);
    }

    /**
     * 取得匹配字符串。
     * @param regexp 正则表达式
     * @param string 输入字符串
     * @return 匹配字符串映射表
     */
    public static Map<Integer, String> groups(String regexp, String string) {

        Map<Integer, String> result = new HashMap<>();
        Matcher matcher = Pattern.compile(regexp).matcher(string);

        if (!matcher.matches()) {
            return result;
        }

        for (int group = 1; group <= matcher.groupCount(); group++) {
            result.put(group, matcher.group(group));
        }

        return result;
    }

}
