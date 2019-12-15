package net.codelet.cloud.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;

/**
 * 反射工具。
 */
public class ReflectionUtils {

    /**
     * 取得类型的属性定义对象。
     * @param clazz     类型
     * @param fieldName 属性名
     * @return 属性对象
     */
    public static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            clazz = clazz.getSuperclass();
            if (clazz == null) {
                throw e;
            }
            return getField(clazz, fieldName);
        }
    }

    /**
     * 取得所有声明的属性。
     * @param clazz 类型
     * @return 属性集合
     */
    public static Collection<Field> getFields(Class clazz) {
        return getFieldMap(clazz).values();
    }

    /**
     * 取得所有声明的属性。
     * @param clazz 类型
     * @return 属性名称与属性的映射表
     */
    public static Map<String, Field> getFieldMap(Class clazz) {
        Map<String, Field> fields = new HashMap<>();
        if (clazz == null) {
            return fields;
        }
        fields.putAll(getFieldMap(clazz.getSuperclass()));
        for (Field field : clazz.getDeclaredFields()) {
            fields.put(field.getName(), field);
        }
        return fields;
    }

    /**
     * 为对象的属性设置值。
     * @param object    设置的对象
     * @param fieldName 设置对象的属性名称
     * @param value     设置对象属性的值
     */
    public static void set(Object object, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(object.getClass(), fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    /**
     * 调用对象的方法。
     * @param object     对象
     * @param methodName 方法名
     * @param arguments  方法参数列表
     * @return 方法执行结果
     */
    public static Object invoke(Object object, String methodName, Object... arguments) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return object.getClass().getDeclaredMethod(methodName).invoke(object, arguments);
    }

    /**
     * 使一个类的方法继承其所实现的接口的相应方法的注解。
     * @param type            类
     * @param interfaceFilter 类所实现的接口的过滤逻辑
     * @param methodFilter    类的方法的过滤逻辑
     * @param annotationTypes 需要继承的注解的类型
     */
    @SafeVarargs
    public static void inheritMethodAnnotationsFromInterface(
        Class<?> type,
        Function<Class<?>, Boolean> interfaceFilter,
        Function<Method, Boolean> methodFilter,
        Class<? extends Annotation>... annotationTypes
    ) {
        for (Class<?> interfaceType : type.getInterfaces()) {
            if (interfaceFilter != null && !interfaceFilter.apply(interfaceType)) {
                continue;
            }
            for (Method superMethod : interfaceType.getDeclaredMethods()) {
                for (Class<? extends Annotation> annotationType : annotationTypes) {
                    Annotation annotation = superMethod.getDeclaredAnnotation(annotationType);
                    if (annotation == null) {
                        continue;
                    }
                    try {
                        Method method = type.getDeclaredMethod(superMethod.getName(), superMethod.getParameterTypes());
                        if (method.getDeclaredAnnotation(annotationType) != null
                            || (methodFilter != null && !methodFilter.apply(method))) {
                            continue;
                        }
                        method.getDeclaredAnnotations();
                        Field declaredAnnotationsField = Executable.class.getDeclaredField("declaredAnnotations");
                        declaredAnnotationsField.setAccessible(true);
                        // noinspection unchecked
                        ((Map<Class<? extends Annotation>, Annotation>) declaredAnnotationsField.get(method)).put(annotationType, annotation);
                    } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException ignored) {
                    }
                }
            }
        }
    }

    /**
     * 取得方法的包括从其实现的接口方法继承的所有注解。
     * @param method                方法
     * @param acceptAnnotationTypes 注解类型数组
     * @return 注解类型与实例的映射表
     */
    @SafeVarargs
    public static Map<Class<? extends Annotation>, Annotation> getAnnotations(
        Method method,
        Class<? extends Annotation>... acceptAnnotationTypes
    ) {
        Map<Class<? extends Annotation>, Annotation> annotationMap = new HashMap<>();
        List<Annotation> annotations = new ArrayList<>();
        Set<Class<? extends Annotation>> annotationTypeSet = acceptAnnotationTypes == null
            ? new HashSet<>()
            : new HashSet<>(Arrays.asList(acceptAnnotationTypes));
        getAnnotations(method, annotationMap, method.getDeclaringClass(), annotationTypeSet);
        return annotationMap;
    }

    /**
     * 取得方法的包括从其实现的接口方法继承的所有注解。
     * @param method                方法
     * @param annotationMap         注解的类型与实例的映射表
     * @param clazz                 类型
     * @param acceptAnnotationTypes 注解类型集合
     */
    private static void getAnnotations(
        Method method, Map<Class<? extends Annotation>,
        Annotation> annotationMap,
        Class<?> clazz,
        Set<Class<? extends Annotation>> acceptAnnotationTypes
    ) {
        if (clazz == null) {
            return;
        }
        if (clazz.getInterfaces() != null) {
            for (Class<?> interfaceType : clazz.getInterfaces()) {
                getAnnotations(method, annotationMap, interfaceType, acceptAnnotationTypes);
                setAnnotationMap(method, annotationMap, interfaceType, acceptAnnotationTypes);
            }
        }
        getAnnotations(method, annotationMap, clazz.getSuperclass(), acceptAnnotationTypes);
        setAnnotationMap(method, annotationMap, clazz, acceptAnnotationTypes);
    }

    /**
     * 从指定的类型中读取所重写的方法的注解，并保存到映射表中。
     * @param method                方法
     * @param annotationMap         注解的类型与实例的映射表
     * @param clazz                 类型
     * @param acceptAnnotationTypes 注解类型集合
     */
    private static void setAnnotationMap(
        Method method,
        Map<Class<? extends Annotation>,
        Annotation> annotationMap,
        Class<?> clazz,
        Set<Class<? extends Annotation>> acceptAnnotationTypes
    ) {
        try {
            Method superMethod = clazz.getMethod(method.getName(), method.getParameterTypes());
            for (Annotation annotation : superMethod.getDeclaredAnnotations()) {
                if (!acceptAnnotationTypes.contains(annotation.annotationType())) {
                    continue;
                }
                annotationMap.put(annotation.annotationType(), annotation);
            }
        } catch (NoSuchMethodException ignored) {
        }
    }

    /**
     * 取得注解的成员属性值的映射表。
     * @param annotation 注解
     * @return 成员属性名称与值的映射表
     */
    public static Map<String, Object> getAnnotationMemberValues(Annotation annotation) {
        if (annotation == null) {
            return null;
        }
        try {
            Field handlerField = Proxy.class.getDeclaredField("h");
            handlerField.setAccessible(true);
            Object handler = handlerField.get(annotation);
            Field memberValuesField = handler.getClass().getDeclaredField("memberValues");
            memberValuesField.setAccessible(true);
            // noinspection unchecked
            return (Map<String, Object>) memberValuesField.get(handler);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return new HashMap<>();
    }
}
