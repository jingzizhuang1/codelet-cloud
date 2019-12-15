package net.codelet.cloud.reports.query.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

public class SubReportEmployeeDTO extends BaseReportDTO {


    private static final long serialVersionUID = 4375992308621050784L;
    @Getter
    @Setter
    @ApiModelProperty("成员姓名")
    private String name;

    @Getter
    @Setter
    @ApiModelProperty("成员年龄")
    private String age;

    @Getter
    @Setter
    @ApiModelProperty("成员手机")
    private String mobile;


}
