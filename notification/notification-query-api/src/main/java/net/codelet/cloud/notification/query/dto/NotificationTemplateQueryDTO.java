package net.codelet.cloud.notification.query.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.SearchPredicate;
import net.codelet.cloud.dto.PaginationDTO;

/**
 * 通知模版查询数据传输对象。
 */
public class NotificationTemplateQueryDTO extends PaginationDTO {

    private static final long serialVersionUID = 748676449777904099L;

    @Getter
    @Setter
    @ApiModelProperty("模版名称")
    @SearchPredicate(value = SearchPredicate.Condition.LIKE, ignoreCase = true)
    private String name;

    @Getter
    @Setter
    @ApiModelProperty("是否已停用")
    private Boolean disabled;
}
