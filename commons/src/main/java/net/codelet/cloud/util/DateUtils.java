package net.codelet.cloud.util;

import net.codelet.cloud.constant.JsonFormatPatterns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具。
 */
public class DateUtils {

    /**
     * 将日期格式化为 ISO 字符串。
     * @param date 日期
     * @return 日期 ISO 字符串
     */
    public static String toISOString(Date date) {
        DateFormat df = new SimpleDateFormat(JsonFormatPatterns.ISO_DATE);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(date);
    }

    /**
     * 将毫秒数转为时间长度文本。
     * @param milliseconds 毫秒数
     * @return 时间长度文本
     */
    public static String toDuration(long milliseconds) {
        long hours = milliseconds / 3600_000L;
        long minutes = (milliseconds / 60_000L) % 60L;
        long seconds = (milliseconds / 1000L) % 60L;
        return String.format("%2d小时%2d分%2d秒", hours, minutes, seconds); // TODO: 本地化
    }
}
