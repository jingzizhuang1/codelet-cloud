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
 * 通知短信发送数据传输对象。
 */
public class NotificationSmsSendDTO extends NotificationSendDTO {

    private static final long serialVersionUID = 8994726651060233736L;

    @Getter
    @Setter
    @NotEmpty
    @Pattern(regexp = RegExpUtils.MOBILE)
    @ApiModelProperty("手机号码")
    private String mobile;

    public NotificationSmsSendDTO() {
    }

    public NotificationSmsSendDTO(ContextDTO context) {
        super(context);
    }

    public NotificationSmsSendDTO(NotificationSendDTO sendDTO) {
        BeanUtils.copyProperties(sendDTO, this);
    }
}
