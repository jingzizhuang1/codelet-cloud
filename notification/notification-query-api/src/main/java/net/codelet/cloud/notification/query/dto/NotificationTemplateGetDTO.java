package net.codelet.cloud.notification.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;

/**
 * 通知模版查询数据传输对象。
 */
public class NotificationTemplateGetDTO extends BaseDTO {

    private static final long serialVersionUID = 6437733332019865719L;

    @Getter
    @Setter
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String id;

    @Getter
    @Setter
    @ApiModelProperty("修订版本号")
    private Long revision;

    @Getter
    @Setter
    @ApiModelProperty("是否已停用")
    private Boolean disabled;
}
