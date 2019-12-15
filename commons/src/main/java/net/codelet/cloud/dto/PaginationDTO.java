package net.codelet.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 查询分页参数数据传输对象。
 */
public class PaginationDTO extends SortDTO {

    private static final long serialVersionUID = -5619513891542926637L;

    @Getter
    @Min(1)
    @Max(100)
    @ApiModelProperty("分页大小")
    private Integer pageSize = 20;

    @Getter
    @Min(1)
    @ApiModelProperty("页号")
    private Integer pageNo = 1;

    @Getter
    @Setter
    @ApiModelProperty(hidden = true)
    private Integer skipCount = null;

    private void updateSkipCount() {
        if (pageSize != null && pageNo != null) {
            skipCount = pageSize * (pageNo - 1);
        }
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        updateSkipCount();
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        updateSkipCount();
    }

    public Pageable pageable() {
        return PageRequest.of(pageNo - 1, pageSize, this.sort());
    }
}
