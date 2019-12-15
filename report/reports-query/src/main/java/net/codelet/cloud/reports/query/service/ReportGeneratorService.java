package net.codelet.cloud.reports.query.service;


import net.codelet.cloud.reports.query.dto.BaseListReportDTO;
import net.codelet.cloud.reports.query.dto.BaseReportDTO;

/**
 * 报表生成器接口
 */
public interface ReportGeneratorService{

    String generateReportFile(String templateFileName, BaseReportDTO reportDto);
    String generateReportFile(String templateFileName, BaseListReportDTO<?> reportDto);
    String generateSubReportFile(String templateFileName, BaseListReportDTO<?> reportDto);
}
