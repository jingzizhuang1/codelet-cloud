package net.codelet.cloud.reports.query.dto;

import java.util.List;

public class SubReportExampleDTO extends BaseListReportDTO {


    private static final long serialVersionUID = 4552781814358389899L;

    private List<SubReportGroupDTO> items;

    public void setItems(List<SubReportGroupDTO> items) {
        this.items = items;
    }

    @Override
    public List<SubReportGroupDTO> getItems() {
        return items;
    }
}
