package net.codelet.cloud.reports.query.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ValueObject {

    /**
     * 取得枚举代码名。
     * @return 枚举代码名
     */
    String name();

    /**
     * 取得代码的表示名。
     * @return 代码表示名
     */
    String getDisplayName();

    /**
     * 取得代码描述。
     * @return 代码描述
     */
    default String getDescription() {
        return null;
    }

}
