package net.codelet.cloud.notification.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.SearchPredicate;
import net.codelet.cloud.dto.PaginationDTO;

import javax.validation.constraints.Pattern;

/**
 * 通知模版查询数据传输对象。
 */
public class NotificationTemplateContentQueryDTO extends PaginationDTO {

    private static final long serialVersionUID = -2242989401737020198L;

    @Getter
    @Setter
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String templateId;

    @Getter
    @Setter
    @ApiModelProperty("关键字")
    @SearchPredicate(
        value = SearchPredicate.Condition.LIKE,
        like = SearchPredicate.LikePosition.MIDDLE,
        propertyNames = {"subject", "content"},
        ignoreCase = true
    )
    private String keyword;

    @Getter
    @Setter
    @Pattern(regexp = "^[a-z]{2}(_[A-Z]{2})?$")
    @ApiModelProperty("语言代码")
    @SearchPredicate(SearchPredicate.Condition.LIKE)
    private String languageCode;
}
