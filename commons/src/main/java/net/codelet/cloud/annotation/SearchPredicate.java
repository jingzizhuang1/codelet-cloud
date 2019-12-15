package net.codelet.cloud.annotation;

import lombok.Getter;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询条件匹配规则：LIKE。
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchPredicate {

    /**
     * 查询条件。
     */
    @AliasFor("condition")
    Condition value() default Condition.EQUAL;

    /**
     * 数据实体的属性名，默认为所标注的属性的名称。
     */
    String[] propertyNames() default {};

    /**
     * 将被忽略的值。
     */
    IgnoreValue ignoreValue() default IgnoreValue.NULL;

    /**
     * <p>设定值在查询条件字符串中的位置。</p>
     * <p>假设值为"Text"，那么：</p>
     * <ul>
     *   <li>当 value 为 START 时，查询条件为 LIKE 'Text%'</li>
     *   <li>当 value 为 MIDDLE 时，查询条件为 LIKE '%Text%'</li>
     *   <li>当 value 为 END 时，查询条件为 LIKE '%Text'</li>
     * </ul>
     */
    LikePosition like() default LikePosition.START;

    /**
     * 是否忽略大小写，默认不忽略。
     */
    boolean ignoreCase() default false;

    /**
     * 查询条件。
     */
    enum Condition {
        EQUAL(false, false),
        NOT_EQUAL(false, false),
        GREATER_THAN(true, false),
        GREATER_THAN_OR_EQUAL_TO(true, false),
        LESS_THAN(true, false),
        LESS_THAN_OR_EQUAL_TO(true, false),
        LIKE(false, true),
        NOT_LIKE(false, true);

        @Getter
        private boolean numeric;

        @Getter
        private boolean textual;

        Condition(boolean numeric, boolean textual) {
            this.numeric = numeric;
            this.textual = textual;
        }
    }

    /**
     * 忽略的值。
     */
    enum IgnoreValue {
        NONE,
        NULL,
        EMPTY_STRING,
        ZERO
    }

    /**
     * 相似匹配位置。
     */
    enum LikePosition {
        START,
        MIDDLE,
        END;

        public String pattern(String text) {
            switch (this) {
                case START:
                    return text + "%";
                case MIDDLE:
                    return "%" + text + "%";
                default:
                    return "%" + text;
            }
        }
    }
}
