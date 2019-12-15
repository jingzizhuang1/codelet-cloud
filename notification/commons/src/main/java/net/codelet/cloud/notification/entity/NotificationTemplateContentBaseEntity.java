package net.codelet.cloud.notification.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.entity.BaseVersionedEntity;

import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通知消息模版本地化内容数据实体。
 */
@MappedSuperclass
public class NotificationTemplateContentBaseEntity extends BaseVersionedEntity {

    private static final long serialVersionUID = -8312955099190789218L;
    private static final String LANGUAGE_CODE_REGEXP = "^([a-z]{2})([^a-z]([a-z]{2}))?$";
    private static final Pattern LANGUAGE_CODE = Pattern.compile(LANGUAGE_CODE_REGEXP, Pattern.CASE_INSENSITIVE);

    @Getter
    @Setter
    @ApiModelProperty("所属模版 ID")
    private String templateId;

    @Getter
    @javax.validation.constraints.Pattern(
        regexp = LANGUAGE_CODE_REGEXP,
        flags = {javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE}
    )
    @ApiModelProperty("语言代码")
    private String languageCode;

    @Getter
    @Setter
    @ApiModelProperty("标题（Velocity 模版）")
    private String subject;

    @Getter
    @Setter
    @ApiModelProperty("内容类型")
    private String contentType;

    @Getter
    @Setter
    @ApiModelProperty("内容（Velocity 模版）")
    private String content;

    @Getter
    @Setter
    @ApiModelProperty("是否已停用")
    private Boolean disabled = false;

    @Getter
    @Setter
    @ApiModelProperty("停用时间")
    private Date disabledAt;

    @Getter
    @Setter
    @ApiModelProperty("停用者 ID")
    private String disabledBy;

    public void setLanguageCode(String languageCode) {
        if (languageCode == null) {
            return;
        }
        Matcher matcher = LANGUAGE_CODE.matcher(languageCode);
        if (!matcher.matches()) {
            return;
        }
        this.languageCode = matcher.group(1) + (matcher.group(3) == null ? "" : ("_" + matcher.group(3).toUpperCase()));
    }
}
