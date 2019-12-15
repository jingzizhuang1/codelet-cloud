package net.codelet.cloud.notification.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 通知消息模版更新表单数据传输对象。
 */
public class NotificationTemplateUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = 8418846728477275637L;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 200)
    @ApiModelProperty("名称")
    private String name;

    @Getter
    @Setter
    @Size(max = 2000)
    @ApiModelProperty("描述")
    private String description;

    @Getter
    @Setter
    @ApiModelProperty("电子邮件或短信发送配置 ID，未指定时使用默认配置")
    private String configurationId;

    @Getter
    @Setter
    @Size(max = 10)
    @ApiModelProperty("标签列表")
    private Set<String> tagList;

    @Getter
    @Setter
    @Valid
    @ApiModelProperty("本地化内容模版")
    private List<NotificationTemplateContentDTO> contents = new ArrayList<>();
}
