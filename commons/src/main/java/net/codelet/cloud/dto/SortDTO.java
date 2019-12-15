package net.codelet.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询分页参数数据传输对象。
 */
public class SortDTO extends BaseDTO {

    private static final long serialVersionUID = 6997275700056672986L;
    private static final Pattern ORDER_BY_PATTERN = Pattern.compile("^([_0-9a-z]+):(asc|desc)$", Pattern.CASE_INSENSITIVE);

    @Getter
    @ApiModelProperty("排序属性，如 name:asc、createdAt:desc")
    private List<String> sortBy;

    @ApiModelProperty(hidden = true)
    private Sort sort = null;

    public void setSortBy(List<String> sortBy) {
        this.sortBy = sortBy;
        sort = null;
        if (sortBy != null && sortBy.size() > 0) {
            List<Sort.Order> orders = new ArrayList<>();
            for (String property : sortBy) {
                Matcher matcher = ORDER_BY_PATTERN.matcher(property);
                if (!matcher.matches()) {
                    return;
                }
                orders.add(new Sort.Order(Sort.Direction.fromString(matcher.group(2)), matcher.group(1)));
            }
            if (orders.size() > 0) {
                sort = Sort.by(orders);
            }
        }
    }

    Sort sort() {
        if (sort == null) {
            sort = Sort.by(Collections.singletonList(new Sort.Order(Sort.Direction.ASC, "id")));
        }
        return sort;
    }
}
