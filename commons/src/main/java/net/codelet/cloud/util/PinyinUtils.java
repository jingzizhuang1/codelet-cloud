package net.codelet.cloud.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 汉字转拼音工具。
 */
public class PinyinUtils {

    private static final HanyuPinyinOutputFormat DEFAULT_FORMAT = new HanyuPinyinOutputFormat();

    static {
        DEFAULT_FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        DEFAULT_FORMAT.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
    }

    /**
     * 将汉字转为拼音，全拼，无音标，无分隔符。
     * @param string 输入字符串
     * @return 转换后的字符串
     */
    public static String convert(String string) {
        try {
            return PinyinHelper
                .toHanYuPinyinString(string, DEFAULT_FORMAT, "", true)
                .toLowerCase();
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            return "";
        }
    }
}
