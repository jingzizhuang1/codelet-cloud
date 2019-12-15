package net.codelet.cloud.reports.query.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SubReportGroupDTO extends BaseListReportDTO {


    private static final long serialVersionUID = -3878990505981337736L;
    @Getter
    @Setter
    @ApiModelProperty("部门名称")
    private String name;

    private List<SubReportTeamDTO> items;

    public void setItems(List<SubReportTeamDTO> items) {
        this.items = items;
    }

    @Override
    public List<SubReportTeamDTO> getItems() {
        return items;
    }
}
