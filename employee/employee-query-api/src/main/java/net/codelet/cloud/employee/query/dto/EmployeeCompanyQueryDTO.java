package net.codelet.cloud.employee.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.SearchPredicate;
import net.codelet.cloud.dto.PaginationDTO;

/**
 * 职员所属公司查询条件数据传输对象。
 */
public class EmployeeCompanyQueryDTO extends PaginationDTO {

    private static final long serialVersionUID = 4680674114365072641L;

    @Getter
    @Setter
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String id;

    @Getter
    @Setter
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String companyId;

    @Getter
    @Setter
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String userId;

    @Getter
    @Setter
    @SearchPredicate(ignoreValue = SearchPredicate.IgnoreValue.NONE)
    @ApiModelProperty("是否已通过申请/接受邀请")
    private Boolean approved = true;
}
