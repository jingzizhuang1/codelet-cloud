package net.codelet.cloud.reports.query.dto;

import java.util.List;

public class ReportListExampleDTO extends BaseListReportDTO {


    private static final long serialVersionUID = 8010277253900633317L;

    private List<ReportListExampleItemDTO> items;

    public void setItems(List<ReportListExampleItemDTO> items) {
        this.items = items;
    }

    @Override
    public List<ReportListExampleItemDTO> getItems() {
        return items;
    }
}
