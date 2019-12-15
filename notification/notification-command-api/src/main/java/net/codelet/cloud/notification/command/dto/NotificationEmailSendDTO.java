package net.codelet.cloud.notification.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.ContextDTO;
import net.codelet.cloud.util.BeanUtils;
import net.codelet.cloud.util.RegExpUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 通知邮件发送数据传输对象。
 */
public class NotificationEmailSendDTO extends NotificationSendDTO {

    private static final long serialVersionUID = 3356721530550755767L;

    @Getter
    @Setter
    @NotEmpty
    @Pattern(regexp = RegExpUtils.EMAIL)
    @ApiModelProperty("电子邮箱地址")
    private String email;

    public NotificationEmailSendDTO() {
    }

    public NotificationEmailSendDTO(ContextDTO context) {
        super(context);
    }

    public NotificationEmailSendDTO(NotificationSendDTO sendDTO) {
        BeanUtils.copyProperties(sendDTO, this);
    }
}
