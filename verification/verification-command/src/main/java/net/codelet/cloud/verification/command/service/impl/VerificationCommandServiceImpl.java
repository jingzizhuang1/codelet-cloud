package net.codelet.cloud.verification.command.service.impl;

import net.codelet.cloud.dto.ContextDTO;
import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.error.*;
import net.codelet.cloud.event.EventPublisher;
import net.codelet.cloud.notification.command.dto.NotificationEmailSendDTO;
import net.codelet.cloud.notification.command.dto.NotificationSendDTO;
import net.codelet.cloud.notification.command.dto.NotificationSmsSendDTO;
import net.codelet.cloud.notification.command.event.NotificationEmailSendEvent;
import net.codelet.cloud.notification.command.event.NotificationSmsSendEvent;
import net.codelet.cloud.parameter.query.logic.ParameterQueryLogic;
import net.codelet.cloud.service.StringRedisService;
import net.codelet.cloud.util.DateUtils;
import net.codelet.cloud.util.RegExpUtils;
import net.codelet.cloud.verification.command.entity.VerificationCommandEntity;
import net.codelet.cloud.verification.command.entity.VerificationConfigurationCommandEntity;
import net.codelet.cloud.verification.command.entity.VerificationSumCommandEntity;
import net.codelet.cloud.verification.command.entity.VerificationTimerCommandEntity;
import net.codelet.cloud.verification.command.logic.VerificationConfigurationCommandLogic;
import net.codelet.cloud.verification.command.repository.VerificationCommandRepository;
import net.codelet.cloud.verification.command.repository.VerificationSumCommandRepository;
import net.codelet.cloud.verification.command.repository.VerificationTimerCommandRepository;
import net.codelet.cloud.verification.command.service.VerificationCommandService;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static net.codelet.cloud.constant.EntityIDs.SYSTEM_USER_ID;

/**
 * 验证码服务。
 */
@Component
public class VerificationCommandServiceImpl extends StringRedisService implements VerificationCommandService {

    private final EventPublisher eventPublisher;
    private final VerificationCommandRepository verificationRepository;
    private final VerificationSumCommandRepository verificationSumRepository;
    private final VerificationConfigurationCommandLogic configurationLogic;
    private final VerificationTimerCommandRepository timerRepository;
    private final ParameterQueryLogic parameterQueryLogic;

    @Autowired
    public VerificationCommandServiceImpl(
        StringRedisTemplate redisTemplate,
        EventPublisher eventPublisher,
        VerificationCommandRepository verificationRepository,
        VerificationSumCommandRepository verificationSumRepository,
        VerificationConfigurationCommandLogic configurationLogic,
        VerificationTimerCommandRepository timerRepository,
        ParameterQueryLogic parameterQueryLogic
    ) {
        super(redisTemplate);
        this.eventPublisher = eventPublisher;
        this.verificationRepository = verificationRepository;
        this.verificationSumRepository = verificationSumRepository;
        this.configurationLogic = configurationLogic;
        this.timerRepository = timerRepository;
        this.parameterQueryLogic = parameterQueryLogic;
    }

    /**
     * 创建验证码并发送电子邮件或短信。
     * <ol>
     *     <li>获取相应的配置信息</li>
     *     <li>
     *         获取计时数据
     *         <ul>
     *             <li>若能够取得计时数据，但已超出计时周期，则将计数器删除</li>
     *             <li>
     *                 若能够取得计时数据，且在计时周期内
     *                 <ul>
     *                     <li>若已到达计时周期内最大发送次数则返回错误</li>
     *                     <li>若尚未到达可再次发送验证码的时间则返回错误</li>
     *                 </ul>
     *             </li>
     *         </ul>
     *     </li>
     *     <li>若未能取得计时器，或计时器已被删除，则生成新的计时器</li>
     *     <li>保存验证码信息</li>
     *     <li>通过指定的方式发送验证码</li>
     * </ol>
     * @param context 请求上下文
     * @param key     电子邮箱地址或手机号码
     * @param purpose 验证码用途
     * @return 可再次发送验证码的 Unix 时间
     */
    @Override
    public Long create(ContextDTO context, String key, VerificationPurpose purpose) {
        OperatorDTO operator = context.getOperator();
        final String receiverId = operator == null ? null : operator.getId();
        final VerificationType keyType;
        final NotificationSendDTO sendDTO;

        // 验证电子邮箱地址时生成32位十六进制验证码
        if (RegExpUtils.isEmailAddress(key)) {
            keyType = VerificationType.EMAIL;
            key = key.toLowerCase();
            sendDTO = new NotificationEmailSendDTO(context);
        // 手机号码时生成6为十进制验证码
        } else if (RegExpUtils.isMobileNo(key)) {
            keyType = VerificationType.MOBILE;
            sendDTO = new NotificationSmsSendDTO(context);
        // 不为电子邮箱地址或手机号码时返回错误
        } else {
            throw new ValidationError("error.verification.key-is-invalid");
        }

        // 取得验证码生成配置信息
        VerificationConfigurationCommandEntity configurationEntity = configurationLogic.get(keyType, purpose);
        if (configurationEntity == null) {
            throw new BusinessError("error.verification.no-configuration");
        }

        final String code = configurationEntity.getCodeCharset().generate(configurationEntity.getCodeLength());
        final long currentTime = System.currentTimeMillis();
        final Date timestamp = new Date(currentTime);

        // 取得发送计时器
        VerificationTimerCommandEntity timerEntity = timerRepository
            .findByKeyTypeAndVerifyKeyAndPurpose(keyType, key, purpose)
            .orElse(null);

        if (timerEntity != null) {
            // 若计时超时，则将计时器删除
            if (currentTime >= timerEntity.getCreatedAt().getTime() + configurationEntity.getRateLimitPeriodMilliseconds()) {
                timerRepository.deleteById(timerEntity.getId());
                timerEntity = null;
            // 否则，根据配置判断当前是否可以发送验证码
            } else {
                // 若已达周期内最大发送次数则返回错误
                if (timerEntity.getTimes() >= configurationEntity.getRateLimitTimes()) {
                    Long nextPeriodFrom = timerEntity.getCreatedAt().getTime() + configurationEntity.getRateLimitPeriodMilliseconds();
                    TooManyRequestsError error = new TooManyRequestsError(
                        "error.verification.rate-limit-times-reached",
                        DateUtils.toDuration(nextPeriodFrom - currentTime)
                    );
                    error.setData(nextPeriodFrom);
                    throw error;
                // 若尚未到达可再次发送的时间则返回错误
                } else if (currentTime < timerEntity.getLastSentAt().getTime() + configurationEntity.getIntervalMilliseconds()) {
                    TooManyRequestsError error = new TooManyRequestsError("error.verification.too-many-operations-in-a-short-period");
                    error.setData(timerEntity.getLastSentAt().getTime() + configurationEntity.getIntervalMilliseconds());
                    throw error;
                }
                // 更新计时器数据
                timerEntity.setLastSentAt(timestamp);
                timerEntity.setTimes(timerEntity.getTimes() + 1);
                timerRepository.save(timerEntity);
            }
        }

        // 若未能取得计时器或计时超时则创建新的计时器
        if (timerEntity == null) {
            timerEntity = new VerificationTimerCommandEntity();
            timerEntity.setKeyType(keyType);
            timerEntity.setVerifyKey(key);
            timerEntity.setPurpose(purpose);
            timerEntity.setCreatedAt(timestamp);
            timerEntity.setLastSentAt(timestamp);
            timerEntity.setTimes(1);
            timerRepository.save(timerEntity);
        }

        // 保存验证码信息
        VerificationCommandEntity entity = new VerificationCommandEntity();
        entity.setVerifyKey(key);
        entity.setKeyType(keyType);
        entity.setPurpose(purpose);
        entity.setCode(code);
        entity.setCreatedAt(timestamp);
        entity.setCreatedBy(receiverId == null ? SYSTEM_USER_ID : receiverId);
        entity.setRemoteAddr(context.getRemoteAddr());
        entity.setUserAgent(context.getUserAgent());
        entity.setExpiresAt(new Date(currentTime + configurationEntity.getTtl() * 1000));
        verificationRepository.save(entity);

        // 设置验证码消息模版相关参数
        sendDTO.setSenderId(SYSTEM_USER_ID);
        sendDTO.setReceiverId(receiverId);
        sendDTO.setTemplateId(configurationEntity.getTemplateId());

        switch (keyType) {
            // 发送电子邮件通知
            case EMAIL:
                NotificationEmailSendDTO emailSendDTO = (NotificationEmailSendDTO) sendDTO;
                emailSendDTO.setEmail(key);
                emailSendDTO.setParameter("email", key);
                sendDTO.setParameter("code", entity.getCode());
                switch (purpose) {
                    case USER_SIGN_IN:
                        sendDTO.setParameter("signInURL", parameterQueryLogic.getValue("sign-in-url"));
                        break;
                    case USER_SIGN_UP:
                        sendDTO.setParameter("signUpURL", parameterQueryLogic.getValue("sign-up-url"));
                        break;
                    case RESET_PASSWORD:
                        sendDTO.setParameter("resetPasswordURL", parameterQueryLogic.getValue("reset-password-url"));
                        break;
                }
                eventPublisher.publish(this, NotificationEmailSendEvent.class, emailSendDTO);
                break;
            // 发送短信通知
            case MOBILE:
                NotificationSmsSendDTO smsSendDTO = (NotificationSmsSendDTO) sendDTO;
                smsSendDTO.setMobile(key);
                sendDTO.setParameter("code", entity.getCode());
                eventPublisher.publish(this, NotificationSmsSendEvent.class, smsSendDTO);
                break;
        }

        // 返回可再次发送验证码的时间的 Unix 时间戳
        return timerEntity.getLastSentAt().getTime() + configurationEntity.getIntervalMilliseconds();
    }

    /**
     * 校验验证码。
     * @param keyType 验证类型
     * @param key     电子邮箱地址或手机号码
     * @param purpose 验证码用途
     * @param code    验证码
     */
    @Override
    public void validate(VerificationType keyType, String key, VerificationPurpose purpose, String code) {
        // 取得验证码生成配置信息
        VerificationConfigurationCommandEntity configurationEntity = configurationLogic.get(keyType, purpose);
        if (configurationEntity == null) {
            throw new BusinessError("error.verification.no-configuration");
        }

        BaseError error = null;

        // 取得验证码信息
        VerificationSumCommandEntity verificationSumEntity = verificationSumRepository
            .findByKeyTypeAndVerifyKeyAndPurpose(keyType, key, purpose).orElse(null);

        // 若验证次数超过限定次数则返回错误
        if (verificationSumEntity != null
            && verificationSumEntity.getVerifiedTimes() >= configurationEntity.getMaxVerifyTimes()) {
            error = new BusinessError("error.verification.validate-too-many-times"); // TODO: set message
        }

        // 若验证码不正确则返回错误
        if (verificationSumEntity == null
            || !verificationSumEntity.getCodeSet().contains(code)) {
            error = new BusinessError("error.verification.code-is-incorrect"); // TODO: set message
        }

        // 若验证码存在则更新验证次数
        if (verificationSumEntity != null && error != null) {
            verificationRepository.increaseVerifyTimes(verificationSumEntity.getId());
        }

        if (error != null) {
            throw error;
        }
    }

    /**
     * 销毁验证码。
     * @param keyType 验证类型
     * @param key     电子邮箱地址或手机号码
     * @param purpose 验证码用途
     * @param code    验证码
     */
    @Override
    @Transactional
    public void delete(VerificationType keyType, String key, VerificationPurpose purpose, String code) {
        VerificationSumCommandEntity verificationSumEntity = verificationSumRepository
            .findByKeyTypeAndVerifyKeyAndPurpose(keyType, key, purpose).orElse(null);
        if (verificationSumEntity == null || !verificationSumEntity.getCodeSet().contains(code)) {
            throw new NotFoundError();
        }
        verificationRepository
            .deleteByKeyTypeAndVerifyKeyAndPurpose(keyType, key, purpose);
    }
}
