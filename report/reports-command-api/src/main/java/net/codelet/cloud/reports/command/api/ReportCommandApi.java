package net.codelet.cloud.reports.command.api;

import net.codelet.cloud.reports.command.dto.ReportTemplateCategoryCreateDTO;
import net.codelet.cloud.reports.command.dto.ReportTemplateCategoryUpdateDTO;
import net.codelet.cloud.reports.command.dto.ReportTemplateCreateDTO;
import net.codelet.cloud.reports.command.dto.ReportTemplateUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "${services.reports.command.name:reports-command}",
    contextId = "reports-command"
)
public interface ReportCommandApi {

    @PostMapping("/report/category")
    void createReportCategory (
        @RequestBody ReportTemplateCategoryCreateDTO reportTemplateCategoryCreateDTO
    );


    @PostMapping("/report/category/{reportTemplateCategoryId}")
    void updateReportCategory (
        @PathVariable String reportTemplateCategoryId,
        @RequestBody ReportTemplateCategoryUpdateDTO reportTemplateCategoryUpdateDTO
    );

    @PostMapping("/report/template")
    void createReportTemplate (
        @RequestBody ReportTemplateCreateDTO reportTemplateCreateDTO
    );


    @PostMapping("/report/template/{reportTemplateId}")
    void updateReportTemplate (
        @PathVariable String reportTemplateId,
        @RequestBody ReportTemplateUpdateDTO reportTemplateUpdateDTO
    );

}
