package net.codelet.cloud.notification.query.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.SearchPredicate;
import net.codelet.cloud.dto.PaginationDTO;

/**
 * 电子邮件发送配置查询参数数据传输对象。
 */
public class MailConfigurationQueryDTO extends PaginationDTO {

    private static final long serialVersionUID = -4849496098014770289L;

    @Getter
    @Setter
    @ApiModelProperty("配置名称")
    @SearchPredicate(SearchPredicate.Condition.LIKE)
    private String name;

    @Getter
    @Setter
    @ApiModelProperty("账号")
    @SearchPredicate(SearchPredicate.Condition.LIKE)
    private String username;
}
