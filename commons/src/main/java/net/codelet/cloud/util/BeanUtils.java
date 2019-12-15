package net.codelet.cloud.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Bean 处理工具。
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

    private static final Map<String, Set<String>> PROPERTY_NAME_CACHE = new HashMap<>();

    /**
     * 合并对象属性值。
     * @param <T>              目标对象类型
     * @param source           源对象
     * @param target           目标对象
     * @param ignoreProperties 忽略属性名数组
     * @return 目标对象是否被更新
     */
    public static <T> Boolean merge(Object source, T target, String... ignoreProperties) {
        if (source == null || target == null) {
            return false;
        }
        Class sourceType = source.getClass();
        Class targetType = target.getClass();
        Set<String> propertyNames = getPropertyNames(sourceType);
        propertyNames.retainAll(getPropertyNames(targetType));
        if (ignoreProperties != null && ignoreProperties.length > 0) {
            propertyNames.removeAll(Arrays.asList(ignoreProperties));
        }
        boolean modified = false;
        for (String propertyName : propertyNames) {
            if (setProperty(source, propertyName, target, targetType) && !modified) {
                modified = true;
            }
        }
        return modified;
    }

    /**
     * 取得指定类型的所有属性名。
     * @param type 类型
     * @return 属性名集合
     */
    public static Set<String> getPropertyNames(final Class<?> type) {
        Set<String> propertyNames = new HashSet<>();
        if (type == null) {
            return propertyNames;
        }
        return new HashSet<>(
            PROPERTY_NAME_CACHE.computeIfAbsent(
                type.getName(),
                key -> getPropertyNames(type, propertyNames)
            )
        );
    }

    /**
     * 取得指定类型的所有属性名。
     * @param type          类型
     * @param propertyNames 属性名集合
     * @return 属性名集合
     */
    private static Set<String> getPropertyNames(
        final Class<?> type,
        final Set<String> propertyNames
    ) {
        if (type == null) {
            return propertyNames;
        }
        getPropertyNames(type.getSuperclass(), propertyNames);
        for (Field field : type.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())
                || Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            propertyNames.add(field.getName());
        }
        return propertyNames;
    }

    /**
     * 取得指定类型的属性。
     * @param <T>          属性范型
     * @param object       对象
     * @param propertyType 属性类型
     * @return 属性值
     */
    public static <T> T getProperty(final Object object, final Class<T> propertyType) {
        if (object == null) {
            return null;
        }
        return getProperty(object, null, propertyType, object.getClass());
    }

    /**
     * 取得指定名称和类型的属性。
     * @param <T>          属性范型
     * @param object       对象
     * @param propertyName 属性名称
     * @return 属性值
     */
    public static <T> T getProperty(final Object object, final String propertyName) {
        if (object == null) {
            return null;
        }
        return getProperty(object, propertyName, null, object.getClass());
    }

    /**
     * 取得指定名称和类型的属性。
     * @param <T>          属性范型
     * @param object       对象
     * @param propertyName 属性名称
     * @param propertyType 属性类型
     * @return 属性值
     */
    public static <T> T getProperty(final Object object, final String propertyName, final Class<T> propertyType) {
        if (object == null) {
            return null;
        }
        return getProperty(object, propertyName, propertyType, object.getClass());
    }

    /**
     * 取得指定类型的属性。
     * @param <T>          属性范型
     * @param object       对象
     * @param propertyName 属性名
     * @param propertyType 属性类型
     * @param type         对象类型
     * @return 属性值
     */
    private static <T> T getProperty(
        final Object object,
        final String propertyName,
        final Class<T> propertyType,
        final Class<?> type
    ) {
        if (type == null) {
            return null;
        }
        for (Field field : type.getDeclaredFields()) {
            if ((propertyName != null && !field.getName().equals(propertyName))
                || (propertyType != null && !field.getType().equals(propertyType))) {
                continue;
            }
            try {
                field.setAccessible(true);
                // noinspection unchecked
                return (T) field.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace(System.err);
            }
        }
        return getProperty(object, propertyName, propertyType, type.getSuperclass());
    }

    /**
     * 设置属性值。
     * @param source       源对象
     * @param propertyName 属性名
     * @param target       目标对象
     * @param targetType   目标对象类型
     * @param <T>          目标对象类型范型
     * @return 是否设置成功
     */
    private static <T> boolean setProperty(Object source, String propertyName, T target, Class<?> targetType) {
        if (targetType == null) {
            return false;
        }
        try {
            Object newValue = getProperty(source, propertyName);
            Field field = targetType.getDeclaredField(propertyName);
            field.setAccessible(true);
            Object originalValue = field.get(target);
            if ((newValue == null && originalValue == null)
                || (newValue != null && newValue.equals(originalValue))) {
                return false;
            }
            field.set(target, newValue);
            return true;
        } catch (NoSuchFieldException e) {
            return setProperty(source, propertyName, target, targetType.getSuperclass());
        } catch (IllegalAccessException e) {
            e.printStackTrace(System.err);
            return false;
        }
    }

    /**
     * 取得第一个不为空指针的值。
     * @param <T>    值范型
     * @param values 值列表
     * @return 第一个不为空指针的值
     */
    public static <T> T ifNull(T... values) {
        for (T value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }
}
