package net.codelet.cloud.notification.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.entity.BaseEntity;
import net.codelet.cloud.util.StringUtils;

import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通知消息模版可用语言代码信息数据实体。
 */
@MappedSuperclass
public class NotificationTemplateLanguageCodesBaseEntity extends BaseEntity {

    private static final long serialVersionUID = -6805104748914289421L;

    @Getter
    @Setter
    @ApiModelProperty("语言代码")
    private String languageCodes;

    public List<String> getLanguageCodeList() {
        if (StringUtils.isEmpty(languageCodes)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(languageCodes.split(",")));
    }
}
