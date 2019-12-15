package net.codelet.cloud.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON API 列表响应数据。
 * @param <T> 返回结果的类型
 */
@JsonPropertyOrder({"success", "meta", "links", "data", "error", "included"})
public class JsonApiListDTO<T extends BaseDTO> extends JsonApiWithDataDTO {

    private static final long serialVersionUID = 6912449131612431358L;

    @Getter
    @Setter
    @ApiModelProperty("分页信息")
    private Meta meta = null;

    @Getter
    @Setter
    @ApiModelProperty("响应数据")
    private List<T> data = new ArrayList<>();

    /**
     * 构造方法。
     */
    public JsonApiListDTO() {
    }

    /**
     * 构造方法。
     * @param data 返回结果
     */
    public JsonApiListDTO(List<T> data) {
        this.setData(data);
    }

    /**
     * 构造方法。
     * @param data 返回结果
     */
    public JsonApiListDTO(Page<T> data) {
        this.setMeta(new Meta(data));
        this.setData(data.getContent());
    }

    /**
     * 分页信息。
     */
    public static class Meta {

        @Getter
        @ApiModelProperty("总件数")
        private Long count;

        @Getter
        @ApiModelProperty("总页数")
        private Integer pages;

        @Getter
        @ApiModelProperty("页号")
        private Integer pageNo;

        @Getter
        @ApiModelProperty("每页件数")
        private Integer pageSize;

        @Getter
        @ApiModelProperty("是否为第一页")
        private Boolean isFirstPage;

        @Getter
        @ApiModelProperty("是否有上一页")
        private Boolean hasPreviousPage;

        @Getter
        @ApiModelProperty("是否为最后一页")
        private Boolean isLastPage;

        @Getter
        @ApiModelProperty("是否有下一页")
        private Boolean hasNextPage;

        public Meta() {
        }

        Meta(Page page) {
            this.count = page.getTotalElements();
            this.pages = page.getTotalPages();
            this.pageNo = page.getNumber() + 1;
            this.pageSize = page.getSize();
            this.isFirstPage = this.pageNo == 1;
            this.hasPreviousPage = !this.isFirstPage;
            this.isLastPage = this.pageNo.equals(this.pages);
            this.hasNextPage = !this.isLastPage;
        }
    }
}
