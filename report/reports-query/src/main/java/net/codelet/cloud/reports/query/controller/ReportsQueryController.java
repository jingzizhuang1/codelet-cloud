package net.codelet.cloud.reports.query.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.reports.query.api.ReportsQueryApi;
import net.codelet.cloud.reports.query.dto.*;
import net.codelet.cloud.reports.query.entity.ReportTemplateCategoryQueryEntity;
import net.codelet.cloud.reports.query.entity.ReportTemplatesQueryEntity;
import net.codelet.cloud.reports.query.service.ReportGeneratorService;
import net.codelet.cloud.reports.query.service.ReportQueryService;
import net.codelet.cloud.reports.query.vo.ReportExportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = {"domain=报表", "biz=报表模板查询&报表示例生成", "responsibility=查询&生成"})
public class ReportsQueryController extends BaseController implements ReportsQueryApi {

    private final ReportGeneratorService reportGeneratorService;
    private final ReportQueryService reportQueryService;

    @Autowired
    public ReportsQueryController(
        ReportGeneratorService reportGeneratorService,
        ReportQueryService reportQueryService
    ) {
        this.reportGeneratorService = reportGeneratorService;
        this.reportQueryService = reportQueryService;
    }


    @Override
    @ApiOperation("生成表单报表示例")
    public ReportsExampleGenerateResultDTO generateFormExample(
        @ApiParam("表单报表数据") ApplicationForInspectionDTO reportDTO
    ) {
        reportDTO.setExportType(ReportExportType.PDF);
        String generatedFileName = reportGeneratorService.generateReportFile("application-for-inspection.jasper", reportDTO);
        ReportsExampleGenerateResultDTO reportsExampleGenerateResultDTO = new ReportsExampleGenerateResultDTO();
        reportsExampleGenerateResultDTO.setReportFileName(generatedFileName);
        return reportsExampleGenerateResultDTO;
    }

    @Override
    @ApiOperation("生成清单报表示例")
    public ReportsExampleGenerateResultDTO generateListExample(
        @ApiParam("清单报表数据") ReportListExampleDTO reportDTO
    ) {
        reportDTO.setExportType(ReportExportType.PDF);
        String generatedFileName = reportGeneratorService.generateReportFile("report-list-example.jasper", reportDTO);
        ReportsExampleGenerateResultDTO reportsExampleGenerateResultDTO = new ReportsExampleGenerateResultDTO();
        reportsExampleGenerateResultDTO.setReportFileName(generatedFileName);
        return reportsExampleGenerateResultDTO;
    }

    @Override
    @ApiOperation("生成嵌套报表示例")
    public ReportsExampleGenerateResultDTO generateSubExample(
        @ApiParam("嵌套报表数据") SubReportExampleDTO reportDTO
    ) {
        reportDTO.setExportType(ReportExportType.PDF);
        String generatedFileName = reportGeneratorService.generateSubReportFile("companies.jasper", reportDTO);
        ReportsExampleGenerateResultDTO reportsExampleGenerateResultDTO = new ReportsExampleGenerateResultDTO();
        reportsExampleGenerateResultDTO.setReportFileName(generatedFileName);
        return reportsExampleGenerateResultDTO;
    }

    @Override
    @ApiOperation("取得报表模板类别列表")
    public List<ReportTemplateCategoryQueryEntity> getReportTemplateCategories() {

        return reportQueryService.getReportTemplateCategories();
    }

    @Override
    @ApiOperation("取得报表模板列表")
    public Page<ReportTemplatesQueryEntity> searchReportTemplates(
        ReportTemplateListQueryDTO reportTemplateListQueryDTO
    ) {
        return reportQueryService.getReportTemplates(reportTemplateListQueryDTO);
    }

    @Override
    @ApiOperation("取得报表模板")
    public ReportTemplatesQueryEntity getReportTemplate(
        @ApiParam("报表模板ID") String reportTemplateId
    ) {
        return reportQueryService.getTemplateInfo(reportTemplateId);
    }


}
