package net.codelet.cloud.notification.query.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.SearchPredicate;
import net.codelet.cloud.dto.PaginationDTO;
import net.codelet.cloud.notification.vo.SmsProvider;

/**
 * 短信发送配置查询参数数据传输对象。
 */
public class SmsConfigurationQueryDTO extends PaginationDTO {

    private static final long serialVersionUID = -7572587958722026136L;

    @Getter
    @Setter
    @ApiModelProperty("服务提供商")
    private SmsProvider provider;

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
