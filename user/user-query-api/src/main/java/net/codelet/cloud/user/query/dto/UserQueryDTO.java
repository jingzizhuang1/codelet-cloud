package net.codelet.cloud.user.query.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.SearchPredicate;
import net.codelet.cloud.dto.PaginationDTO;

/**
 * 用户查询条件。
 */
public class UserQueryDTO extends PaginationDTO {

    private static final long serialVersionUID = 525970986347610489L;

    @Getter
    @Setter
    @SearchPredicate(value = SearchPredicate.Condition.LIKE, propertyNames = {"name", "namePinyin"})
    @ApiModelProperty("姓名")
    private String name;
}
