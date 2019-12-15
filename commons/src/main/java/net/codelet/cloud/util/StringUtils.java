package net.codelet.cloud.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.io.IOUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;

/**
 * 字符串工具。
 */
public class StringUtils {

    private static final char[] HEX_CHARSET = "0123456789abcdef".toCharArray();

    /**
     * 检查字符串是否为空白。
     * @param string 检查对象字符串
     * @param trim   是否去除首尾空白字符
     * @return 字符串是否为空白
     */
    public static boolean isBlank(String string, boolean trim) {
        return string != null && "".equals(trim ? string.trim() : string);
    }

    /**
     * 检查字符串是否为空白。
     * @param string 检查对象字符串
     * @return 字符串是否为空白
     */
    public static boolean isBlank(String string) {
        return isBlank(string, false);
    }

    /**
     * 检查字符串是否为空。
     * @param string 检查对象字符串
     * @param trim   是否去除首尾空白字符
     * @return 字符串是否为空
     */
    public static boolean isEmpty(String string, boolean trim) {
        return string == null || isBlank(string, trim);
    }

    /**
     * 检查字符串是否为空。
     * @param string 检查对象字符串
     * @return 字符串是否为空
     */
    public static boolean isEmpty(String string) {
        return isEmpty(string, false);
    }

    /**
     * 去除首尾空白字符。
     * @param string 输入字符串
     * @return 去除首尾空白字符后的字符串
     */
    public static String trim(String string) {
        return trim(string, "");
    }

    /**
     * 去除首尾空白字符。
     * @param string       输入字符串
     * @param defaultValue 当为空指针或空字符串时的默认值
     * @return 去除首尾空白字符后的字符串
     */
    public static String trim(String string, String defaultValue) {

        if (isEmpty(string, true)) {
            return defaultValue;
        }

        return string.trim();
    }

    /**
     * 根据字节数组生成十六进制字符串。
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    public static String toHex(byte[] bytes) {

        char[] hexChars = new char[bytes.length * 2];

        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_CHARSET[v >>> 4];
            hexChars[i * 2 + 1] = HEX_CHARSET[v & 0x0F];
        }

        return new String(hexChars);
    }

    /**
     * 截取字符串中开始指定个数的字符。
     * @param string 输入字符串
     * @param chars  截取字符数
     * @return 截取后的字符串
     */
    public static String first(String string, int chars) {
        return string.substring(0, Math.min(chars, string.length()));
    }

    /**
     * 截取字符串中最后指定个数的字符。
     * @param string 输入字符串
     * @param chars  截取字符数
     * @return 截取后的字符串
     */
    public static String last(String string, int chars) {

        int startAt = string.length() - chars;

        if (startAt < 0) {
            startAt = 0;
        }

        return string.substring(startAt, startAt + chars);
    }

    /**
     * URL 内容编码。
     * @param string 输入字符串
     * @return 编码后的字符串
     */
    public static String encodeURIComponent(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return string;
        }
    }

    /**
     * 将对象转为键值对列表。
     * @param parentKey 上级名称
     * @param object    输入值
     * @return 键值对列表
     */
    private static List<String> toNameValuePairs(
        String parentKey,
        Object object
    ) {

        List<String> kvps = new ArrayList<>();

        if (object == null) {
            return kvps;
        }

        if (DataTypeUtils.isPrimitive(object)) {
            kvps.add(parentKey + "=" + encodeURIComponent(object.toString()));
            return kvps;
        }

        Map map = (Map) toMap(object);

        if (map == null) {
            return kvps;
        }

        if (parentKey != null && !"".equals(parentKey)) {
            parentKey += ".";
        } else {
            parentKey = "";
        }

        Set keys = map.keySet();
        String name;
        Object value;

        for (Object key : keys) {

            name = parentKey + key.toString();
            value = map.get(key);

            if (value == null) {
                continue;
            }

            if (value instanceof Map) {
                kvps.addAll(toNameValuePairs(name, value));
            } else if (value instanceof Iterable) {
                for (Object item : (Iterable) value) {
                    kvps.addAll(toNameValuePairs(name, item));
                }
            } else {
                kvps.add(
                    key.toString()
                        + "="
                        + encodeURIComponent(value.toString())
                );
            }

        }

        return kvps;
    }

    /**
     * 将对象转为 URL Encoded 字符串。
     * @param object 输入值
     * @return URL Encoded 字符串
     */
    public static String toURLEncoded(Object object) {
        return String.join("&", toNameValuePairs(null, object));
    }

    /**
     * 将对象转为 Map。
     * @param object 输入值
     * @return 转换后的值
     */
    public static Object toMap(Object object) {

        if (DataTypeUtils.isPrimitive(object)) {
            return object;
        }

        if (object.getClass().isArray()) {
            object = Arrays.asList((Object[]) object);
        }

        if (object instanceof Iterable) {

            List<Object> list = new ArrayList<>();

            for (Object o : (Iterable) object) {
                list.add(toMap(o));
            }

            return list;
        }

        if (object instanceof Map) {

            Map source = (Map) object;
            Map<String, Object> map = new HashMap<>();

            Set keys = source.keySet();

            for (Object key : keys) {
                map.put(key.toString(), toMap(source.get(key)));
            }

            return map;
        }

        Map<String, Object> map = new HashMap<>();

        BeanInfo info;
        Method reader;
        String propertyName;
        Object propertyValue;

        try {
            info = Introspector.getBeanInfo(object.getClass());
        } catch (IntrospectionException e) {
            return null;
        }

        for (PropertyDescriptor property : info.getPropertyDescriptors()) {

            reader =  property.getReadMethod();

            if (reader == null) {
                continue;
            }

            try {
                propertyValue = reader.invoke(object);
            } catch (ReflectiveOperationException e) {
                continue;
            }

            propertyName = property.getName();

            if ("class".equals(propertyName)) {
                continue;
            }

            map.put(propertyName, toMap(propertyValue));
        }

        return map;
    }

    /**
     * 将对象转为 JSON 字符串。
     * @param object 对象
     * @param pretty 是否格式化
     * @return JSON 字符串
     */
    public static String toJSON(Object object, boolean pretty) {

        ObjectWriter writer = (new ObjectMapper()).writer();

        if (pretty) {
            writer = writer.withDefaultPrettyPrinter();
        }

        try {
            return writer.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 将对象转为 JSON 字符串。
     * @param object 对象
     * @return JSON 字符串
     */
    public static String toJSON(Object object) {
        return toJSON(object, false);
    }

    /**
     * 将输入流转为字符串。
     * @param stream 输入流
     * @return 字符串
     */
    public static String fromInputStream(InputStream stream) {
        try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(stream, writer, "UTF-8");
            return writer.toString();
        } catch (IOException e) {
            return "";
        }
    }
}
