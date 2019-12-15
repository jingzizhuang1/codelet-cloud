package net.codelet.cloud.reports.query.service.impl;

import com.google.common.collect.Maps;
import net.codelet.cloud.reports.query.dto.BaseListReportDTO;
import net.codelet.cloud.reports.query.dto.BaseReportDTO;
import net.codelet.cloud.reports.query.dto.SubReportExampleDTO;
import net.codelet.cloud.reports.query.service.ReportGeneratorService;
import net.codelet.cloud.reports.query.vo.ReportExportType;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ReportGeneratorServiceImpl implements ReportGeneratorService {

    private final static Logger logger = LoggerFactory.getLogger(ReportGeneratorServiceImpl.class);

    // 生成报表文件存放路径
    @Value("${application.static.reports}")
    private String generatedReportsDir;

    // 报表模板存放路径 TODO 设定为外部不可访问路径
    @Value("${application.static.reports.templates.jasper}")
    private String reportTemplateJasperFileDir;

    @Override
    public String generateReportFile(String templateFileName, BaseReportDTO reportDto) {

        Map<String, Object> parameters = bean2Map(reportDto);

        return generateReportFile(templateFileName, parameters,  null, reportDto.getExportType());
    }

    @Override
    public String generateReportFile(String templateFileName, BaseListReportDTO<?> reportDto) {

        Map<String, Object> parameters = bean2Map(reportDto);

        return generateReportFile(templateFileName, parameters,  reportDto.getItems(), reportDto.getExportType());
    }

    @Override
    public String generateSubReportFile(String templateFileName, BaseListReportDTO<?> reportDto) {

        Map<String, Object> parameters = new HashMap<>();
        if (reportDto instanceof SubReportExampleDTO) {
            parameters.put("SUB_REPORT1", reportTemplateJasperFileDir + "companies-teams.jasper");
            parameters.put("SUB_REPORT2", reportTemplateJasperFileDir + "companies-employees.jasper");
        }

        return generateReportFile(templateFileName, parameters, reportDto.getItems(), reportDto.getExportType());
    }

    private String generateReportFile(String templateFileName, Map<String, Object> parameters, List<?> records, ReportExportType exportType) {

        if (exportType == null) {
            // 默认导出PDF
            exportType = ReportExportType.PDF;
        }

        String reportFileName = null;

        // 生成报表文件
        try {
            // 从 resources 中提取模板文件
            InputStream template =  new FileInputStream(new File(reportTemplateJasperFileDir + templateFileName));
            File outputFile = new File(generatedReportsDir + UUID.randomUUID().toString().replaceAll("-","") + exportType.getExtName());
            logger.info("outputFile.getPath() = " + outputFile.getPath());

            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            OutputStream report = new FileOutputStream(outputFile);
            final JRDataSource dataSource = (records == null || records.size() == 0)
                ? new JREmptyDataSource()
                : new JRBeanCollectionDataSource(records);

            reportFileName = outputFile.getName();
            // 生成PDF文档
            if (exportType == ReportExportType.PDF) {

                if (parameters == null) {
                    parameters = new HashMap<>();
                }

                JasperRunManager.runReportToPdfStream(template, report, parameters, dataSource);



            } else if (exportType == ReportExportType.MS_EXCEL || exportType == ReportExportType.MS_WORD) {


                final JRAbstractExporter exporter;


                if (exportType == ReportExportType.MS_EXCEL) {
                    exporter = new JRXlsxExporter();
                } else {
                    exporter = new JRDocxExporter();
                }

                final JasperPrint jasperPrint;

                if (dataSource == null) {
                    jasperPrint = JasperFillManager.fillReport(template, parameters);
                } else {
                    jasperPrint = JasperFillManager.fillReport(template, parameters, dataSource);
                }

                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(report));
                exporter.exportReport();
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return reportFileName;
    }


    private <T> Map<String, Object> bean2Map(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key+"", beanMap.get(key));
            }
        }
        return map;
    }

}
