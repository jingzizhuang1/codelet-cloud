package net.codelet.cloud.employee.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * 加入申请驳回表单。
 */
public class MembershipApplicationRejectDTO extends BaseDTO {

    private static final long serialVersionUID = -3878719269060604597L;

    @Getter
    @Setter
    @ApiModelProperty("驳回理由")
    private String comment;
}
