package net.codelet.cloud.verification.command.aspect;

import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.ValidateVerificationCode;
import net.codelet.cloud.aspect.BaseAspect;
import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.dto.verification.EmailVerificationCodeDTO;
import net.codelet.cloud.dto.verification.SmsVerificationCodeDTO;
import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.error.ValidationError;
import net.codelet.cloud.util.AspectUtils;
import net.codelet.cloud.util.ReflectionUtils;
import net.codelet.cloud.util.RegExpUtils;
import net.codelet.cloud.util.StringUtils;
import net.codelet.cloud.verification.command.api.VerificationCommandApi;
import net.codelet.cloud.verification.vo.VerificationType;
import net.codelet.cloud.vo.VerificationPurpose;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 电子邮件/短信验证码校验。
 */
@Aspect
@Component
public class ValidateValidationCodeAspect extends BaseAspect {

    private final VerificationCommandApi verificationCommandApi;
    private static final String VERIFICATION_CODES_KEY = "net.codelet.cloud.RequestAttributes.VERIFICATION_CODES";

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ValidateValidationCodeAspect(VerificationCommandApi verificationCommandApi) {
        this.verificationCommandApi = verificationCommandApi;
    }

    /**
     * 定义切入点：使用 @ValidateVerificationCode 注解的方法。
     */
    @Pointcut("@annotation(annotation)")
    public void controller(ValidateVerificationCode annotation) {
    }

    /**
     * 校验电子邮件/短信验证码。
     * @param point      切入点信息
     * @param annotation 注解设置
     */
    @Before(value = "controller(annotation)", argNames = "point,annotation")
    public void doBefore(
        final JoinPoint point,
        final ValidateVerificationCode annotation
    ) {
        HttpServletRequest request = getRequest();
        OperatorDTO operator = getOperator();
        if (request == null || (operator != null && operator.isAdministrator())) {
            return;
        }

        // 若设置了指定的参数的值，使其被设置时不进行验证码校验，则结束
        for (ValidateVerificationCode.Parameter parameterConflictWith : annotation.passWhenParameterPresent()) {
            // noinspection unchecked
            Object parameterObjectConflictWith = AspectUtils.getParameter(point, parameterConflictWith.type());
            try {
                Field propertyConflictWith = ReflectionUtils.getField(parameterConflictWith.type(), parameterConflictWith.propertyName());
                propertyConflictWith.setAccessible(true);
                Object propertyConflictWithValue = propertyConflictWith.get(parameterObjectConflictWith);
                if (propertyConflictWithValue != null
                    && !(propertyConflictWithValue instanceof String && StringUtils.isEmpty((String) propertyConflictWithValue))) {
                    return;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace(System.err);
            }
        }

        List<VerificationCode> verificationCodes = new ArrayList<>();
        request.setAttribute(VERIFICATION_CODES_KEY, verificationCodes);

        AspectUtils.ParameterHolder parameters = AspectUtils.getParameters(point, annotation.keyParameterNames());
        String emailAddress = null;
        String mobileNo = null;

        for (String keyName : annotation.keyParameterNames()) {
            if (RegExpUtils.isEmailAddress(parameters.getAsString(keyName))) {
                emailAddress = parameters.getAsString(keyName);
            } else if (RegExpUtils.isMobileNo(parameters.getAsString(keyName))) {
                mobileNo = parameters.getAsString(keyName);
            }
        }

        // 校验电子邮件验证码
        EmailVerificationCodeDTO emailVerificationCodeDTO
            = AspectUtils.getParameter(point, EmailVerificationCodeDTO.class);
        if (emailVerificationCodeDTO != null) {
            if (!StringUtils.isEmpty(emailVerificationCodeDTO.getEmail())) {
                emailAddress = emailVerificationCodeDTO.getEmail();
            }
            if (emailAddress != null) {
                validate(
                    VerificationType.EMAIL,
                    emailAddress,
                    annotation.value(),
                    emailVerificationCodeDTO.getEmailVerificationCode(),
                    verificationCodes
                );
            }
        }

        // 校验短信验证码
        SmsVerificationCodeDTO smsVerificationCodeDTO
            = AspectUtils.getParameter(point, SmsVerificationCodeDTO.class);
        if (smsVerificationCodeDTO != null) {
            if (!StringUtils.isEmpty(smsVerificationCodeDTO.getMobile())) {
                mobileNo = smsVerificationCodeDTO.getMobile();
            }
            if (mobileNo != null) {
                validate(
                    VerificationType.MOBILE,
                    mobileNo,
                    annotation.value(),
                    smsVerificationCodeDTO.getSmsVerificationCode(),
                    verificationCodes
                );
            }
        }
    }

    /**
     * 校验验证码。
     * @param keyType           验证类型
     * @param verifyKey         电子邮箱地址/手机号码等
     * @param purpose           验证码用途
     * @param code              验证码
     * @param verificationCodes 通过校验的验证码信息的列表
     */
    private void validate(
        VerificationType keyType,
        String verifyKey,
        VerificationPurpose purpose,
        String code,
        List<VerificationCode> verificationCodes
    ) {
        if (StringUtils.isEmpty(verifyKey)) {
            return;
        }
        if (StringUtils.isEmpty(code)) {
            throw new ValidationError("error.verification.code-is-required"); // TODO: set message
        }
        verificationCommandApi.validate(keyType, verifyKey, purpose, code);
        verificationCodes.add(new VerificationCode(keyType, verifyKey, code));
    }

    /**
     * 请求被成功该处理后销毁验证码数据。
     * @param annotation 注解设置
     */
    @AfterReturning(value = "controller(annotation)", argNames = "annotation")
    public void doAfter(final ValidateVerificationCode annotation) {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return;
        }
        // noinspection unchecked
        List<VerificationCode> verificationCodes = (List<VerificationCode>) request.getAttribute(VERIFICATION_CODES_KEY);
        if (verificationCodes == null || verificationCodes.size() == 0) {
            return;
        }
        verificationCodes.forEach(verificationCode -> {
            try {
                verificationCommandApi.delete(
                    verificationCode.type,
                    verificationCode.key,
                    annotation.value(),
                    verificationCode.code
                );
            } catch (NotFoundError ignored) {
            }
        });
    }

    /**
     * 验证码信息。
     */
    private static class VerificationCode {

        @Getter
        @Setter
        private VerificationType type;

        @Getter
        @Setter
        private String key;

        @Getter
        @Setter
        private String code;

        VerificationCode(VerificationType type, String key, String code) {
            this.type = type;
            this.key = key;
            this.code = code;
        }
    }
}
