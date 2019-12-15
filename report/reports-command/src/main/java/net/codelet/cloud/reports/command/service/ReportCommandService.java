package net.codelet.cloud.reports.command.service;

import net.codelet.cloud.reports.command.dto.ReportTemplateCategoryCreateDTO;
import net.codelet.cloud.reports.command.dto.ReportTemplateCategoryUpdateDTO;
import net.codelet.cloud.reports.command.dto.ReportTemplateCreateDTO;
import net.codelet.cloud.reports.command.dto.ReportTemplateUpdateDTO;

public interface ReportCommandService {

    void createReportTemplate(ReportTemplateCreateDTO reportTemplateCreateDTO);

    void updateReportTemplate(String reportTemplateId, ReportTemplateUpdateDTO reportTemplateUpdateDTO);

    void createReportTemplateCategory(ReportTemplateCategoryCreateDTO reportTemplateCategoryCreateDTO);

    void updateReportTemplateCategory(String reportTemplateCategoryId, ReportTemplateCategoryUpdateDTO reportTemplateCategoryUpdateDTO);

}
