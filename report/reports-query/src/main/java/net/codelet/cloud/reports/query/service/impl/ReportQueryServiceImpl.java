package net.codelet.cloud.reports.query.service.impl;

import net.codelet.cloud.reports.query.dto.ReportTemplateListQueryDTO;
import net.codelet.cloud.reports.query.entity.ReportTemplateCategoryQueryEntity;
import net.codelet.cloud.reports.query.entity.ReportTemplatesQueryEntity;
import net.codelet.cloud.reports.query.repository.ReportTemplateCategoryQueryRepository;
import net.codelet.cloud.reports.query.repository.ReportTemplateQueryRepository;
import net.codelet.cloud.reports.query.service.ReportQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportQueryServiceImpl implements ReportQueryService {

    private final static Logger logger = LoggerFactory.getLogger(ReportQueryServiceImpl.class);
    private final ReportTemplateQueryRepository reportTemplateQueryRepository;
    private final ReportTemplateCategoryQueryRepository reportTemplateCategoryQueryRepository;

    @Autowired
    public ReportQueryServiceImpl(
        ReportTemplateQueryRepository reportTemplateQueryRepository,
        ReportTemplateCategoryQueryRepository reportTemplateCategoryQueryRepository
    ) {
        this.reportTemplateQueryRepository = reportTemplateQueryRepository;
        this.reportTemplateCategoryQueryRepository = reportTemplateCategoryQueryRepository;
    }


    @Override
    public List<ReportTemplateCategoryQueryEntity> getReportTemplateCategories() {
        List<ReportTemplateCategoryQueryEntity> reportTemplateCategoryQueryEntities = (List<ReportTemplateCategoryQueryEntity>) reportTemplateCategoryQueryRepository.findAll();
        return reportTemplateCategoryQueryEntities;
    }

    @Override
    public Page<ReportTemplatesQueryEntity> getReportTemplates(ReportTemplateListQueryDTO reportTemplateListQueryDTO) {
        Page<ReportTemplatesQueryEntity> reportTemplatesQueryEntities = (Page<ReportTemplatesQueryEntity>) reportTemplateQueryRepository.findAllByCriteria(reportTemplateListQueryDTO, reportTemplateListQueryDTO.pageable());
        return reportTemplatesQueryEntities;
    }


    @Override
    public ReportTemplatesQueryEntity getTemplateInfo(String reportTemplateId) {

        return this.reportTemplateQueryRepository.findById(reportTemplateId).get();
    }


}
