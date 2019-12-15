package net.codelet.cloud.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 人机验证码校验注解。
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateCaptcha {

    /**
     * 用于标明注解所修饰的接口是否必须进行人机验证。
     * @return 是否必须进行人机验证，默认必须
     */
    boolean required() default true;
}
