package net.codelet.cloud.verification.query.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.annotation.SetReferencedEntities;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.verification.query.api.VerificationConfigurationQueryApi;
import net.codelet.cloud.verification.query.dto.VerificationConfigurationQueryDTO;
import net.codelet.cloud.verification.query.entity.VerificationConfigurationQueryEntity;
import net.codelet.cloud.verification.query.service.VerificationConfigurationQueryService;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = {"domain=验证码", "biz=验证码配置", "responsibility=查询"})
public class VerificationConfigurationQueryController extends BaseController implements VerificationConfigurationQueryApi {

    private final VerificationConfigurationQueryService verificationConfigurationQueryService;

    @Autowired
    public VerificationConfigurationQueryController(
        VerificationConfigurationQueryService verificationConfigurationQueryService
    ) {
        this.verificationConfigurationQueryService = verificationConfigurationQueryService;
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @SetReferencedEntities
    @ApiOperation("查询验证码配置")
    public Page<VerificationConfigurationQueryEntity> search(@Valid VerificationConfigurationQueryDTO queryDTO) {
        return verificationConfigurationQueryService.search(queryDTO);
    }

    @Override
    @CheckUserPrivilege(administrator = true)
    @SetReferencedEntities
    @ApiOperation("取得验证码配置详细信息")
    public VerificationConfigurationQueryEntity get(
        @ApiParam("验证类型") VerificationType keyType,
        @ApiParam("验证码用途") VerificationPurpose purpose
    ) {
        return verificationConfigurationQueryService.get(keyType, purpose);
    }
}
