package net.codelet.cloud.util;

import net.codelet.cloud.config.MessageSourceConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * 本地化消息处理工具。
 */
public class I18nUtils {

    private static final MessageSource MESSAGE_SOURCE = MessageSourceConfiguration.MESSAGE_SOURCE;
    private static final String[] NO_PARAMETER = new String[] {};

    /**
     * 设置消息源，更新消息内容。
     * @param parameters 消息参数
     */
    public static String message(String code, String... parameters) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            return MESSAGE_SOURCE.getMessage(code, resolveParameters(parameters, locale), locale);
        } catch (NoSuchMessageException e) {
            return code;
        }
    }

    /**
     * 设置消息参数。
     * @param keys   消息名
     * @param locale 地区
     * @return 参数列表
     */
    private static String[] resolveParameters(String[] keys, Locale locale) {
        if (keys == null || keys.length == 0) {
            return new String[] {};
        }

        String[] parameters = new String[keys.length];

        for (int i = 0; i < keys.length; i++) {
            try {
                parameters[i] = MESSAGE_SOURCE.getMessage(keys[i], NO_PARAMETER, locale);
            } catch (NoSuchMessageException e) {
                parameters[i] = keys[i];
            }
        }

        return parameters;
    }
}
