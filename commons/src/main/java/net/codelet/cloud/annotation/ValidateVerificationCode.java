package net.codelet.cloud.annotation;

import net.codelet.cloud.vo.VerificationPurpose;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * REST 控制器短信/电子邮件验证码检查注解。
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateVerificationCode {

    /**
     * 验证码用途。
     */
    VerificationPurpose value();

    /**
     * 验证 KEY（电子邮箱地址/手机号码）的参数名称。
     */
    String[] keyParameterNames() default {};

    /**
     * 存在指定的属性时不进行校验。
     */
    Parameter[] passWhenParameterPresent() default {};

    /**
     * 参数注解。
     */
    @interface Parameter {

        /**
         * 参数的所属类型。
         */
        Class type();

        /**
         * 参数的属性名。
         */
        String propertyName();
    }
}
