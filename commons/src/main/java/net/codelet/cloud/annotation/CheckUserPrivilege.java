package net.codelet.cloud.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * REST 控制器用户权限检查注解。
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckUserPrivilege {

    /** 客户端是否必须设置授权信息（Authorization 请求头） */
    boolean required() default true;

    /** 是否要求必须为系统管理员 */
    boolean administrator() default false;

    /** 是否必须为资源的所有者 */
    boolean owner() default false;

    /** 资源所有者用户 ID 的路径参数名 */
    String ownerId() default "userId";

    /** 是否必须为组织的成员 */
    boolean member() default false;

    /** 资源所属组织 ID 的路径参数名 */
    String orgId() default "orgId";

    /** 所需权限列表 */
    String[] privileges() default {};
}
