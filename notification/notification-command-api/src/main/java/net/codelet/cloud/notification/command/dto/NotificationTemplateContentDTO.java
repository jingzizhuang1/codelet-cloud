package net.codelet.cloud.notification.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

/**
 * 通知消息模版本地化内容。
 */
public class NotificationTemplateContentDTO extends NotificationTemplateContentSaveDTO {

    private static final long serialVersionUID = -4314617661824160182L;

    @Getter
    @Setter
    @Size(max = 5)
    @ApiModelProperty("语言代码（如 en、en_US、zh、zh_CN、zh_TW、ja、ko 等）")
    private String languageCode;
}
