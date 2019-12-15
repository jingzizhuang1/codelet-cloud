package net.codelet.cloud.reports.query.dto;

import net.codelet.cloud.reports.query.vo.ReportExportType;

import java.util.List;

public abstract class BaseListReportDTO<T extends BaseReportListItemDTO> extends BaseReportDTO {

    private static final long serialVersionUID = -7204698914423619430L;

    public BaseListReportDTO() {
        super();
    }

    public abstract List<T> getItems();

    private ReportExportType exportType = ReportExportType.PDF;

    public ReportExportType getExportType() {
        return exportType;
    }

    public void setExportType(ReportExportType exportType) {
        this.exportType = exportType;
    }

}
