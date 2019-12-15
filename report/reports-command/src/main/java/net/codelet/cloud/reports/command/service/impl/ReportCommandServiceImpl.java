package net.codelet.cloud.reports.command.service.impl;

import net.codelet.cloud.reports.command.dto.ReportTemplateCategoryCreateDTO;
import net.codelet.cloud.reports.command.dto.ReportTemplateCategoryUpdateDTO;
import net.codelet.cloud.reports.command.dto.ReportTemplateCreateDTO;
import net.codelet.cloud.reports.command.dto.ReportTemplateUpdateDTO;
import net.codelet.cloud.reports.command.entity.ReportTemplateCategoryCommandEntity;
import net.codelet.cloud.reports.command.entity.ReportTemplateCommandEntity;
import net.codelet.cloud.reports.command.repository.ReportTemplateCategoryCommandRepository;
import net.codelet.cloud.reports.command.repository.ReportTemplateCommandRepository;
import net.codelet.cloud.reports.command.service.ReportCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReportCommandServiceImpl implements ReportCommandService {

    private final ReportTemplateCategoryCommandRepository reportTemplateCategoryCommandRepository;

    private final ReportTemplateCommandRepository reportTemplateCommandRepository;


    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ReportCommandServiceImpl(
        ReportTemplateCategoryCommandRepository reportTemplateCategoryCommandRepository,
        ReportTemplateCommandRepository reportTemplateCommandRepository
    ) {

        this.reportTemplateCategoryCommandRepository = reportTemplateCategoryCommandRepository;
        this.reportTemplateCommandRepository = reportTemplateCommandRepository;
    }

    @Override
    public void createReportTemplate(ReportTemplateCreateDTO reportTemplateCreateDTO) {

        ReportTemplateCommandEntity reportTemplateCommandEntity = new ReportTemplateCommandEntity();
        reportTemplateCommandEntity.setCategoryId(reportTemplateCreateDTO.getCategoryId());
        reportTemplateCommandEntity.setDataType(reportTemplateCreateDTO.getDataType());
        reportTemplateCommandEntity.setDescription(reportTemplateCreateDTO.getDescription());
        reportTemplateCommandEntity.setExampleFileName(reportTemplateCreateDTO.getExampleFileName());
        reportTemplateCommandEntity.setTemplateFileName(reportTemplateCreateDTO.getTemplateFileName());
        reportTemplateCommandEntity.setReportTemplateName(reportTemplateCreateDTO.getReportTemplateName());
        reportTemplateCommandEntity.setUpdateTime(reportTemplateCreateDTO.getUpdateTime());
        reportTemplateCommandEntity.updateRevision();

        this.reportTemplateCommandRepository.save(reportTemplateCommandEntity);

    }

    @Override
    public void updateReportTemplate(String reportTemplateId, ReportTemplateUpdateDTO reportTemplateUpdateDTO) {
        Optional<ReportTemplateCommandEntity> reportTemplateCommandEntity = this.reportTemplateCommandRepository.findById(reportTemplateId);

        reportTemplateCommandEntity.get().setCategoryId(reportTemplateUpdateDTO.getCategoryId());
        reportTemplateCommandEntity.get().setDataType(reportTemplateUpdateDTO.getDataType());
        reportTemplateCommandEntity.get().setDescription(reportTemplateUpdateDTO.getDescription());
        reportTemplateCommandEntity.get().setExampleFileName(reportTemplateUpdateDTO.getExampleFileName());
        reportTemplateCommandEntity.get().setTemplateFileName(reportTemplateUpdateDTO.getTemplateFileName());
        reportTemplateCommandEntity.get().setReportTemplateName(reportTemplateUpdateDTO.getReportTemplateName());
        reportTemplateCommandEntity.get().setUpdateTime(reportTemplateUpdateDTO.getUpdateTime());
        reportTemplateCommandEntity.get().updateRevision();

        this.reportTemplateCommandRepository.save(reportTemplateCommandEntity.get());
    }

    @Override
    public void createReportTemplateCategory(ReportTemplateCategoryCreateDTO reportTemplateCategoryCreateDTO) {
        ReportTemplateCategoryCommandEntity reportTemplateCategoryCommandEntity = new ReportTemplateCategoryCommandEntity();
        reportTemplateCategoryCommandEntity.setCategoryName(reportTemplateCategoryCreateDTO.getCategoryName());
        reportTemplateCategoryCommandEntity.updateRevision();
        this.reportTemplateCategoryCommandRepository.save(reportTemplateCategoryCommandEntity);
    }

    @Override
    public void updateReportTemplateCategory(String reportTemplateCategoryId, ReportTemplateCategoryUpdateDTO reportTemplateCategoryUpdateDTO) {
        Optional<ReportTemplateCategoryCommandEntity> reportTemplateCategoryCommandEntity = this.reportTemplateCategoryCommandRepository.findById(reportTemplateCategoryId);
        reportTemplateCategoryCommandEntity.get().setCategoryName(reportTemplateCategoryUpdateDTO.getCategoryName());
        reportTemplateCategoryCommandEntity.get().updateRevision();
        this.reportTemplateCategoryCommandRepository.save(reportTemplateCategoryCommandEntity.get());
    }
}
