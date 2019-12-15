package net.codelet.cloud.reports.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.SearchPredicate;
import net.codelet.cloud.dto.PaginationDTO;

/**
 * 报表模板列表查询DTO。
 */
public class ReportTemplateListQueryDTO extends PaginationDTO {

    private static final long serialVersionUID = 525970986347610489L;

    @Getter
    @Setter
    @JsonIgnore
    @ApiModelProperty("报表模板所属类别ID")
    private String categoryId;

    @Getter
    @Setter
    @ApiModelProperty("关键字（检索范围：模板名称，模板描述）")
    @SearchPredicate(
        value = SearchPredicate.Condition.LIKE,
        like = SearchPredicate.LikePosition.MIDDLE,
        propertyNames = {"reportTemplateName", "description"},
        ignoreCase = true
    )
    private String keyword;

}
