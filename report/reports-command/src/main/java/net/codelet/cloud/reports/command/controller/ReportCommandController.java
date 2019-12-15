package net.codelet.cloud.reports.command.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.reports.command.api.ReportCommandApi;
import net.codelet.cloud.reports.command.dto.ReportTemplateCategoryCreateDTO;
import net.codelet.cloud.reports.command.dto.ReportTemplateCategoryUpdateDTO;
import net.codelet.cloud.reports.command.dto.ReportTemplateCreateDTO;
import net.codelet.cloud.reports.command.dto.ReportTemplateUpdateDTO;
import net.codelet.cloud.reports.command.service.ReportCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"domain=报表", "biz=报表模板", "responsibility=命令"})
public class ReportCommandController extends BaseController implements ReportCommandApi {

    private final ReportCommandService reportCommandService;

    @Autowired
    public ReportCommandController(
        ReportCommandService reportCommandService
    ) {
        this.reportCommandService = reportCommandService;
    }

    @Override
    @ApiOperation("创建报表模板类别")
    public void createReportCategory(
        @ApiParam("模板类别信息") ReportTemplateCategoryCreateDTO reportTemplateCategoryCreateDTO
    ) {
        this.reportCommandService.createReportTemplateCategory(reportTemplateCategoryCreateDTO);
    }

    @Override
    @ApiOperation("更新报表模板类别")
    public void updateReportCategory(
        @ApiParam("报表模板类别ID") String reportTemplateCategoryId,
        @ApiParam("报表模板类别信息") ReportTemplateCategoryUpdateDTO reportTemplateCategoryUpdateDTO
    ) {
        this.reportCommandService.updateReportTemplateCategory(reportTemplateCategoryId, reportTemplateCategoryUpdateDTO);
    }

    @Override
    @ApiOperation("创建报表模板")
    public void createReportTemplate(
        @ApiParam("报表模板信息") ReportTemplateCreateDTO reportTemplateCreateDTO
    ) {
        this.reportCommandService.createReportTemplate(reportTemplateCreateDTO);
    }

    @Override
    @ApiOperation("更新报表模板")
    public void updateReportTemplate(
        @ApiParam("报表模板ID")  String reportTemplateId,
        @ApiParam("报表模板信息") ReportTemplateUpdateDTO reportTemplateUpdateDTO
    ) {
        this.reportCommandService.updateReportTemplate(reportTemplateId, reportTemplateUpdateDTO);
    }
}
