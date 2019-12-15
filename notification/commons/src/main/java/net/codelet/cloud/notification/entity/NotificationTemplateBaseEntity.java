package net.codelet.cloud.notification.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.entity.BaseVersionedEntity;
import net.codelet.cloud.util.StringUtils;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 通知消息模版数据实体。
 */
@MappedSuperclass
public class NotificationTemplateBaseEntity extends BaseVersionedEntity {

    private static final long serialVersionUID = 6039182261213406472L;

    @Getter
    @Setter
    @ApiModelProperty("名称")
    private String name;

    @Getter
    @Setter
    @ApiModelProperty("描述")
    private String description;

    @Getter
    @Setter
    @ApiModelProperty("电子邮件或短信发送配置 ID，未指定时使用默认配置")
    private String configurationId;

    @Getter
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String tags;

    @Getter
    @Transient
    @ApiModelProperty("标签")
    private Set<String> tagList = new HashSet<>();

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

    public void setTags(String tags) {
        this.tags = tags;
        tagList.clear();
        if (!StringUtils.isEmpty(tags)) {
            tagList.addAll(Arrays.asList(tags.split(",")));
        }
    }

    public void setTagList(Set<String> tagList) {
        this.tagList.clear();
        if (tagList == null || tagList.size() == 0) {
            this.tags = null;
            return;
        }
        this.tagList.addAll(tagList);
        this.tags = String.join(",", tagList);
    }
}
