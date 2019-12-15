package net.codelet.cloud.reports.query.dto;

import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.reports.query.vo.ReportExportType;

/**
 * 制作报表内容（基础信息）
 */
public class BaseReportDTO extends BaseDTO {
    private static final long serialVersionUID = 8469893940226443320L;

    private ReportExportType exportType = ReportExportType.PDF;

    public ReportExportType getExportType() {
        return exportType;
    }

    public void setExportType(ReportExportType exportType) {
        this.exportType = exportType;
    }

}
