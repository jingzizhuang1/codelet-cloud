package net.codelet.cloud.notification.query.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.annotation.SetReferencedEntities;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.notification.query.api.MailConfigurationQueryApi;
import net.codelet.cloud.notification.query.dto.MailConfigurationQueryDTO;
import net.codelet.cloud.notification.query.entity.MailConfigurationQueryEntity;
import net.codelet.cloud.notification.query.service.MailConfigurationQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = {"domain=通知", "biz=电子邮件发送配置", "responsibility=查询"})
public class MailConfigurationQueryController extends BaseController implements MailConfigurationQueryApi {

    private final MailConfigurationQueryService mailConfigurationQueryService;

    @Autowired
    public MailConfigurationQueryController(
        MailConfigurationQueryService mailConfigurationQueryService
    ) {
        this.mailConfigurationQueryService = mailConfigurationQueryService;
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @SetReferencedEntities
    @ApiOperation("查询电子邮件发送配置")
    public Page<MailConfigurationQueryEntity> search(
        @Valid MailConfigurationQueryDTO queryDTO
    ) {
        return mailConfigurationQueryService.search(queryDTO);
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @SetReferencedEntities
    @ApiOperation("取得默认的电子邮件发送配置详细信息")
    public MailConfigurationQueryEntity getDefault() {
        return mailConfigurationQueryService.getDefault();
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @SetReferencedEntities
    @ApiOperation("取得电子邮件发送配置详细信息")
    public MailConfigurationQueryEntity get(
        @ApiParam("配置 ID") String configurationId
    ) {
        return mailConfigurationQueryService.get(configurationId);
    }
}
