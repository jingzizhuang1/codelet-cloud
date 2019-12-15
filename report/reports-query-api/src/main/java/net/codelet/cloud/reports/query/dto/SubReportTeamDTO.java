package net.codelet.cloud.reports.query.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SubReportTeamDTO extends BaseListReportDTO {


    private static final long serialVersionUID = -8815498177895045832L;
    @Getter
    @Setter
    @ApiModelProperty("项目组名称")
    private String name;

    private List<SubReportEmployeeDTO> items;

    public void setItems(List<SubReportEmployeeDTO> items) {
        this.items = items;
    }

    @Override
    public List<SubReportEmployeeDTO> getItems() {
        return items;
    }
}
