package net.codelet.cloud.reports.query.api;

import net.codelet.cloud.reports.query.dto.*;
import net.codelet.cloud.reports.query.entity.ReportTemplateCategoryQueryEntity;
import net.codelet.cloud.reports.query.entity.ReportTemplatesQueryEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 用户查询 REST 接口。
 */
@FeignClient(
    name = "${services.reports.query.name:reports-query}",
    contextId = "reports-query"
)
public interface ReportsQueryApi {


    @PostMapping("/reports/example/form")
    ReportsExampleGenerateResultDTO generateFormExample(
        @RequestBody ApplicationForInspectionDTO reportDTO
    );


    @PostMapping("/reports/example/list")
    ReportsExampleGenerateResultDTO generateListExample(
        @RequestBody ReportListExampleDTO reportDTO
    );


    @PostMapping("/reports/example/sub")
    ReportsExampleGenerateResultDTO generateSubExample(
        @RequestBody SubReportExampleDTO reportDTO
    );

    /**
     * 获取报表模板种类列表
     */
    @GetMapping("/report/categories")
    List<ReportTemplateCategoryQueryEntity> getReportTemplateCategories();

    /**
     * 获取报表模板列表
     */
    @GetMapping("/report/templates")
    Page<ReportTemplatesQueryEntity> searchReportTemplates(
        ReportTemplateListQueryDTO reportTemplateListQueryDTO
    );

    /**
     * 获取报表模板
     * @param reportTemplateId 模板id
     */
    @GetMapping("/report/template/{templateId}")
    ReportTemplatesQueryEntity getReportTemplate(
        @PathVariable("templateId") String reportTemplateId
    );



}
