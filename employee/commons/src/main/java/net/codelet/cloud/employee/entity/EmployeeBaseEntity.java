package net.codelet.cloud.employee.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.ReferenceEntity;
import net.codelet.cloud.entity.BaseVersionedEntity;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class EmployeeBaseEntity extends BaseVersionedEntity {

    private static final long serialVersionUID = 192878055150687L;

    @Getter
    @Setter
    @ReferenceEntity("organization")
    @JsonProperty("company")
    @ApiModelProperty("公司 ID")
    private String companyId;

    @Getter
    @Setter
    @ReferenceEntity("user")
    @JsonProperty("user")
    @ApiModelProperty("用户 ID")
    private String userId;

    @Getter
    @Setter
    @ApiModelProperty("职员编号")
    private String employeeNo;

    @Getter
    @Setter
    @ApiModelProperty("是否已通过")
    private Boolean approved;

    @Getter
    @Setter
    @ApiModelProperty("通过时间")
    private Date approvedAt;

    @Getter
    @Setter
    @ReferenceEntity("user")
    @ApiModelProperty("通过操作者")
    private String approvedBy;

    @Getter
    @Setter
    @ApiModelProperty("拒绝时间")
    private Date rejectedAt;

    @Getter
    @Setter
    @ReferenceEntity("user")
    @ApiModelProperty("拒绝操作者")
    private String rejectedBy;

    @Getter
    @Setter
    @ApiModelProperty("批注")
    private String comment;
}
