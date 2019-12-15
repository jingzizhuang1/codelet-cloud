package net.codelet.cloud.reports.query.vo;



/**
 * 报表输出文件格式。
 */
public enum ReportExportType implements ValueObject {

    PDF("PDF", ".pdf"),
    MS_WORD("Microsoft Word", ".docx"),
    MS_EXCEL("Microsoft Excel", ".xlsx");

    private String displayName;
    private String extName;

    ReportExportType(String displayName, String extName) {
        this.displayName = displayName;
        this.extName = extName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public String getExtName() {
        return extName;
    }
}
