package net.codelet.cloud.aspect;

import net.codelet.cloud.annotation.ValidateCaptcha;
import net.codelet.cloud.dto.CaptchaValidateDTO;
import net.codelet.cloud.error.ValidationError;
import net.codelet.cloud.util.AspectUtils;
import net.codelet.cloud.util.CaptchaUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 人机验证数据校验。
 */
@Aspect
@Component
public class ValidateCaptchaAspect extends BaseAspect {

    /**
     * 定义切入点：使用 @ValidateCaptcha 注解的方法。
     */
    @Pointcut("@annotation(annotation)")
    public void controller(ValidateCaptcha annotation) {
    }

    /**
     * 校验人机验证数据。
     * @param point      切入点信息
     * @param annotation 注解设置
     */
    @Before(
        value = "controller(annotation)",
        argNames = "point,annotation"
    )
    public void doBefore(
        final JoinPoint point,
        final ValidateCaptcha annotation
    ) {
        CaptchaValidateDTO validateDTO = AspectUtils
            .getParameter(point, CaptchaValidateDTO.class);

        if (validateDTO == null || validateDTO.getCaptcha() == null) {
            if (annotation.required()) {
                throw new ValidationError("error.captcha.required"); // TODO: set error message
            }
            return;
        }

        CaptchaUtils.verify(getContext(), validateDTO.getCaptcha());
    }
}
