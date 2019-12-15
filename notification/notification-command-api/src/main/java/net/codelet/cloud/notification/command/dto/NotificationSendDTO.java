package net.codelet.cloud.notification.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.dto.ContextDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 通知消息发送数据传输对象。
 */
public class NotificationSendDTO extends BaseDTO {

    private static final long serialVersionUID = 5472571736617245777L;

    @Getter
    @Setter
    @ApiModelProperty(value = "发送者用户 ID", hidden = true)
    private String senderId;

    @Getter
    @Setter
    @ApiModelProperty("接收者用户 ID")
    private String receiverId;

    @Getter
    @Setter
    @ApiModelProperty("语言代码")
    private List<String> acceptLanguages = new ArrayList<>();

    @Getter
    @Setter
    @ApiModelProperty("消息模版 ID")
    private String templateId;

    @Getter
    @Setter
    @ApiModelProperty("消息参数")
    private Map<String, Object> parameters = new HashMap<>();

    public NotificationSendDTO() {
    }

    public NotificationSendDTO(ContextDTO context) {
        HttpServletRequest request = context.getRequest();
        if (request == null) {
            return;
        }
        Enumeration<Locale> locales = request.getLocales();
        String language;
        while (locales.hasMoreElements()) {
            language = locales.nextElement().toString();
            if (acceptLanguages.indexOf(language) < 0) {
                acceptLanguages.add(language);
            }
        }
        System.out.println("\0001B[1;97;41m " + String.join(",", acceptLanguages) + " \0001B[0m");
    }

    /**
     * 设置消息参数。
     * @param parameterName  参数名
     * @param parameterValue 参数值
     */
    public void setParameter(String parameterName, Object parameterValue) {
        parameters.put(parameterName, parameterValue);
    }
}
