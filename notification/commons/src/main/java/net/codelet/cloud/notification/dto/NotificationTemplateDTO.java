package net.codelet.cloud.notification.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.notification.entity.NotificationTemplateContentBaseEntity;
import net.codelet.cloud.util.StringUtils;
import net.codelet.cloud.util.TemplateUtils;

/**
 * 通知模版缓存对象。
 */
public class NotificationTemplateDTO {

    @Getter
    @Setter
    @ApiModelProperty("电子邮件或短信发送配置 ID，未指定时使用默认配置")
    private String configurationId;

    @Getter
    @Setter
    @ApiModelProperty("标题")
    private String subject;

    @Getter
    @Setter
    @ApiModelProperty("模版")
    private String content;

    @Getter
    @Setter
    @ApiModelProperty("内容类型")
    private String contentType;

    public NotificationTemplateDTO() {
    }

    public Boolean isHTML() {
        return "text/html".equals(contentType);
    }

    public NotificationTemplateDTO(String configurationId, NotificationTemplateContentBaseEntity contentEntity) {
        this.configurationId = configurationId;
        this.subject = contentEntity.getSubject();
        this.content = contentEntity.getContent();
        this.contentType = contentEntity.getContentType();
    }

    /**
     * 渲染标题。
     * @param parameters 模版参数
     * @return 标题文本
     */
    public String renderSubject(Object... parameters) {
        return render(subject, parameters);
    }

    /**
     * 渲染内容。
     * @param parameters 模版参数
     * @return 内容文本
     */
    public String renderContent(Object... parameters) {
        return render(content, parameters);
    }

    /**
     * 渲染模版。
     * @param template   模版
     * @param parameters 模版参数
     * @return 渲染结果
     */
    private String render(String template, Object... parameters) {
        if (StringUtils.isEmpty(template)) {
            return "";
        }
        if (parameters == null || parameters.length == 0) {
            return template;
        }
        return TemplateUtils.render(template, parameters);
    }
}
