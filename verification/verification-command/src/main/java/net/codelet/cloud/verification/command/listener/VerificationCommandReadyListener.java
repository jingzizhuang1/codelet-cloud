package net.codelet.cloud.verification.command.listener;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import net.codelet.cloud.event.EventPublisher;
import net.codelet.cloud.notification.command.dto.NotificationTemplateContentDTO;
import net.codelet.cloud.notification.command.dto.NotificationTemplateCreateDTO;
import net.codelet.cloud.notification.command.entity.NotificationTemplateCommandEntity;
import net.codelet.cloud.notification.command.event.NotificationTemplateCreateEvent;
import net.codelet.cloud.notification.command.event.NotificationTemplateCreatedEvent;
import net.codelet.cloud.util.BeanUtils;
import net.codelet.cloud.util.CaptchaUtils;
import net.codelet.cloud.util.IOUtils;
import net.codelet.cloud.verification.command.dto.VerificationConfigurationCreateDTO;
import net.codelet.cloud.verification.command.dto.VerificationConfigurationUpdateDTO;
import net.codelet.cloud.verification.command.service.VerificationConfigurationCommandService;
import net.codelet.cloud.verification.vo.VerificationCharset;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.messaging.MessageDeliveryException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.codelet.cloud.verification.vo.VerificationCharset.HEXADECIMAL;
import static net.codelet.cloud.verification.vo.VerificationCharset.NUMBER;

/**
 * 应用初始化。
 */
@Configuration
public class VerificationCommandReadyListener {

    private static final Integer EMAIL_VERIFICATION_CODE_LENGTH = 16; // 电子邮件验证码长度
    private static final VerificationCharset EMAIL_VERIFICATION_CODE_CHARSET = HEXADECIMAL; // 电子邮件验证码字符集
    private static final Integer EMAIL_VERIFICATION_CODE_TTL = 1800; // 电子邮件验证码有效时长：三十分钟
    private static final Integer EMAIL_VERIFICATION_CODE_SEND_INTERVAL = 60; // 电子邮件验证码发送最小时间间隔：一分钟
    private static final Integer EMAIL_RATE_LIMIT_PERIOD = 3600; // 电子邮件验证码发送频率限制周期：一小时
    private static final Integer EMAIL_RATE_LIMIT_TIMES = 10; // 电子邮件验证码发送频率限制周期内发送最大次数

    private static final Integer SMS_VERIFICATION_CODE_LENGTH = 6; // 短信验证码长度
    private static final VerificationCharset SMS_VERIFICATION_CODE_CHARSET = NUMBER; // 短信验证码字符集
    private static final Integer SMS_VERIFICATION_CODE_TTL = 300; // 短信验证码有效时长：五分钟
    private static final Integer SMS_VERIFICATION_CODE_SEND_INTERVAL = 60; // 短信验证码发送最小时间间隔：一分钟
    private static final Integer SMS_RATE_LIMIT_PERIOD = 86400; // 短信验证码发送频率限制周期：一天
    private static final Integer SMS_RATE_LIMIT_TIMES = 3; // 短信验证码发送频率限制周期内发送最大次数

    private static final Pattern HTML_TITLE_PATTERN = Pattern.compile("<title>(.+?)</title>", Pattern.CASE_INSENSITIVE);

    private final Environment environment;
    private final EventPublisher eventPublisher;
    private final VerificationConfigurationCommandService verificationConfigurationCommandService;

    @Autowired
    public VerificationCommandReadyListener(
        Environment environment,
        EventPublisher eventPublisher,
        VerificationConfigurationCommandService verificationConfigurationCommandService
    ) {
        this.environment = environment;
        this.eventPublisher = eventPublisher;
        this.verificationConfigurationCommandService = verificationConfigurationCommandService;
    }

    /**
     * 设置图形验证码字体文件路径。
     */
    @EventListener(ApplicationReadyEvent.class)
    public void setCaptchaFontDir() {
        CaptchaUtils.setFonts(environment.getProperty("application.resources.captcha-fonts"));
    }

    /**
     * 创建所需消息模版并创建验证码发送配置。
     */
    @EventListener(ApplicationReadyEvent.class)
    public void createNotificationTemplates() {
        // 电子邮件验证码配置
        VerificationConfigurationCreateDTO emailConfigDTO = new VerificationConfigurationCreateDTO(
            VerificationType.EMAIL,
            EMAIL_VERIFICATION_CODE_LENGTH,
            EMAIL_VERIFICATION_CODE_CHARSET,
            EMAIL_VERIFICATION_CODE_TTL,
            EMAIL_VERIFICATION_CODE_SEND_INTERVAL,
            EMAIL_RATE_LIMIT_PERIOD,
            EMAIL_RATE_LIMIT_TIMES
        );

        // 短信验证码配置
        VerificationConfigurationCreateDTO smsConfigDTO = new VerificationConfigurationCreateDTO(
            VerificationType.MOBILE,
            SMS_VERIFICATION_CODE_LENGTH,
            SMS_VERIFICATION_CODE_CHARSET,
            SMS_VERIFICATION_CODE_TTL,
            SMS_VERIFICATION_CODE_SEND_INTERVAL,
            SMS_RATE_LIMIT_PERIOD,
            SMS_RATE_LIMIT_TIMES
        );

        try {
            // 用户注册
            createNotificationTemplate(
                "用户注册邮箱地址验证邮件模版",
                emailConfigDTO,
                VerificationPurpose.USER_SIGN_UP,
                new String[] {"en", "text/html", "/templates/email/user-sign-up/en.html"},
                new String[] {"zh_CN", "text/html", "/templates/email/user-sign-up/zh-cn.html"}
            );
            createNotificationTemplate(
                "用户注册手机号码验证短信模版",
                smsConfigDTO,
                VerificationPurpose.USER_SIGN_UP,
                new String[] {"en", "text/plain", "/templates/sms/user-sign-up/en.txt"},
                new String[] {"zh_CN", "text/plain", "/templates/sms/user-sign-up/zh-cn.txt"}
            );

            // 用户登录
            createNotificationTemplate(
                "用户登录手机号码验证短信模版",
                smsConfigDTO,
                VerificationPurpose.USER_SIGN_IN,
                new String[] {"en", "text/plain", "/templates/sms/user-sign-in/en.txt"},
                new String[] {"zh_CN", "text/plain", "/templates/sms/user-sign-in/zh-cn.txt"}
            );

            // 密码重置
            createNotificationTemplate(
                "重置密码邮箱地址验证邮件模版",
                emailConfigDTO,
                VerificationPurpose.RESET_PASSWORD,
                new String[] {"en", "text/html", "/templates/email/reset-password/en.html"},
                new String[] {"zh_CN", "text/html", "/templates/email/reset-password/zh-cn.html"}
            );
            createNotificationTemplate(
                "重置密码手机号码验证短信模版",
                smsConfigDTO,
                VerificationPurpose.RESET_PASSWORD,
                new String[] {"en", "text/plain", "/templates/sms/reset-password/en.txt"},
                new String[] {"zh_CN", "text/plain", "/templates/sms/reset-password/zh-cn.txt"}
            );
        } catch (MessageDeliveryException e) {
            // TODO: 通过事件判断是否可以发布消息
        }
    }

    /**
     * 创建模版。
     * @param name     模版名称
     * @param purpose  验证码用途
     * @param config   验证码配置信息
     * @param contents 模版本地化内容数组
     */
    private void createNotificationTemplate(
        final String name,
        final VerificationConfigurationCreateDTO config,
        final VerificationPurpose purpose,
        final String[]... contents
    ) {
        // 若已存在配置则结束
        if (verificationConfigurationCommandService.exists(config.getKeyType(), purpose, null)) {
            return;
        }

        // 构造模版数据
        NotificationTemplateCreateDTO templateDTO = new NotificationTemplateCreateDTO();
        templateDTO.setName(name);
        templateDTO.setTagList(new HashSet<>(Arrays.asList(config.getKeyType().name(), purpose.name())));

        // 设置本地化内容列表
        for (String[] properties : contents) {
            NotificationTemplateContentDTO contentDTO = new NotificationTemplateContentDTO();
            String content = IOUtils.readAsString(getClass().getResourceAsStream(properties[2]));
            Matcher matcher = HTML_TITLE_PATTERN.matcher(content == null ? "" : content);
            contentDTO.setLanguageCode(properties[0]);
            if (matcher.find()) {
                contentDTO.setSubject(matcher.group(1));
            }
            contentDTO.setContentType(properties[1]);
            contentDTO.setContent(content);
            templateDTO.getContents().add(contentDTO);
        }

        // 设置通过 NotificationTemplateCreatedEvent 事件回传的数据
        VerificationConfigurationCreateDTO additional = new VerificationConfigurationCreateDTO();
        BeanUtils.copyProperties(config, additional);
        additional.setPurpose(purpose);
        eventPublisher.publish(this, NotificationTemplateCreateEvent.class, templateDTO, additional);
    }

    /**
     * 通知消息模版创建完成后更新验证码配置信息。
     * @param event 通知消息模版创建完成事件
     */
    @EventListener
    public void onNotificationTemplateCreated(NotificationTemplateCreatedEvent event) {
        // 取得回传的数据
        VerificationConfigurationCreateDTO additional
            = event.getAdditional(VerificationConfigurationCreateDTO.class);

        // 创建模版
        NotificationTemplateCommandEntity templateEntity = event.getPayload();

        // 创建验证码配置
        try {
            VerificationConfigurationUpdateDTO configurationDTO = new VerificationConfigurationUpdateDTO();
            BeanUtils.copyProperties(additional, configurationDTO);
            configurationDTO.setTemplateId(templateEntity.getId());
            verificationConfigurationCommandService.set(
                null,
                additional.getKeyType(),
                additional.getPurpose(),
                null,
                configurationDTO
            );
        } catch (HystrixRuntimeException e) {
            e.printStackTrace(System.err);
        }
    }
}
