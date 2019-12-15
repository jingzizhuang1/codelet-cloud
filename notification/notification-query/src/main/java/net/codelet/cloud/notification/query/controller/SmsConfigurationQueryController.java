package net.codelet.cloud.notification.query.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.annotation.SetReferencedEntities;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.notification.query.api.SmsConfigurationQueryApi;
import net.codelet.cloud.notification.query.dto.SmsConfigurationQueryDTO;
import net.codelet.cloud.notification.query.entity.SmsConfigurationQueryEntity;
import net.codelet.cloud.notification.query.service.SmsConfigurationQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = {"domain=通知", "biz=短信发送配置", "responsibility=查询"})
public class SmsConfigurationQueryController extends BaseController implements SmsConfigurationQueryApi {

    private final SmsConfigurationQueryService smsConfigurationQueryService;

    @Autowired
    public SmsConfigurationQueryController(
        SmsConfigurationQueryService smsConfigurationQueryService
    ) {
        this.smsConfigurationQueryService = smsConfigurationQueryService;
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @SetReferencedEntities
    @ApiOperation("查询短信发送配置")
    public Page<SmsConfigurationQueryEntity> search(
        @Valid SmsConfigurationQueryDTO queryDTO
    ) {
        return smsConfigurationQueryService.search(queryDTO);
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @SetReferencedEntities
    @ApiOperation("取得默认的短信发送配置详细信息")
    public SmsConfigurationQueryEntity getDefault() {
        return smsConfigurationQueryService.getDefault();
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @SetReferencedEntities
    @ApiOperation("取得短信发送配置详细信息")
    public SmsConfigurationQueryEntity get(
        @ApiParam("配置 ID") String configurationId
    ) {
        return smsConfigurationQueryService.get(configurationId);
    }
}
