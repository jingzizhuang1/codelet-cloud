package net.codelet.cloud.reports.query.service;


import net.codelet.cloud.reports.query.dto.ReportTemplateListQueryDTO;
import net.codelet.cloud.reports.query.entity.ReportTemplateCategoryQueryEntity;
import net.codelet.cloud.reports.query.entity.ReportTemplatesQueryEntity;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 报表数据查询
 */
public interface ReportQueryService {

    List<ReportTemplateCategoryQueryEntity> getReportTemplateCategories();

    Page<ReportTemplatesQueryEntity> getReportTemplates(ReportTemplateListQueryDTO reportTemplateListQueryDTO);

    ReportTemplatesQueryEntity getTemplateInfo(String reportTemplateId);


}
