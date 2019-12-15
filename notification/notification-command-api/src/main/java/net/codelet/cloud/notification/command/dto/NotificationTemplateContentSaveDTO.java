package net.codelet.cloud.notification.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 通知消息模版本地化内容。
 */
public class NotificationTemplateContentSaveDTO extends BaseDTO {

    private static final long serialVersionUID = -8352085160717906148L;

    @Getter
    @Setter
    @Size(max = 255)
    @ApiModelProperty("标题模版（Velocity 模版）")
    private String subject;

    @Getter
    @Setter
    @Size(max = 255)
    @Pattern(regexp = "^text/(plain|html)$")
    @ApiModelProperty("内容类型（如 text/plain、text/html 等）")
    private String contentType;

    @Getter
    @Setter
    @Size(max = 32768)
    @ApiModelProperty("内容模版（Velocity 模版）")
    private String content;
}
