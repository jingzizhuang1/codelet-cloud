package net.codelet.cloud.verification.command.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.codelet.cloud.controller.BaseController;
import net.codelet.cloud.util.CaptchaUtils;
import net.codelet.cloud.verification.command.api.CaptchaCommandApi;
import net.codelet.cloud.verification.command.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"domain=验证码", "biz=人机验证码", "responsibility=命令"})
public class CaptchaCommandController extends BaseController implements CaptchaCommandApi {

    private final CaptchaService captchaService;

    @Autowired
    public CaptchaCommandController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @Override
    @ApiOperation("获取人机验证数据")
    public CaptchaUtils.CaptchaData get() {
        return captchaService.generate(getContext());
    }

    @Override
    @ApiOperation("校验人机验证识别结果")
    public void validate(CaptchaUtils.CaptchaDTO captchaDTO) {
        captchaService.verify(getContext(), captchaDTO);
    }
}
