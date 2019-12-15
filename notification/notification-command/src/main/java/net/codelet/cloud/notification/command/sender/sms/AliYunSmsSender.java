package net.codelet.cloud.notification.command.sender.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.codelet.cloud.notification.command.entity.SmsConfigurationCommandEntity;

import java.util.Map;

/**
 * 阿里云短信发送工具。
 * @link https://help.aliyun.com/document_detail/101414.html?spm=a2c4g.11186623.6.617.26ce50a4CTQ2Rw
 * @link https://help.aliyun.com/document_detail/112148.html?spm=a2c4g.11186623.6.647.1d9a50a4HXAOm5
 */
public class AliYunSmsSender implements SmsSender {

    private static final String ACTION = "SendSms";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String REGION = "cn-hangzhou";
    private static final String VERSION = "2017-05-25";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final IAcsClient client;
    private final String signName;
    private final Long revision;

    /**
     * 构造方法。
     * @param configuration 短信发送配置
     */
    public AliYunSmsSender(SmsConfigurationCommandEntity configuration) {
        this.client = new DefaultAcsClient(
            DefaultProfile.getProfile(
                REGION,
                configuration.getUsername(),
                configuration.getPassword()
            )
        );
        this.signName = configuration.getSignName();
        this.revision = configuration.getRevision();
    }

    /**
     * 发送短信。
     * @param content      短信内容
     * @param parameters   参数表
     * @param phoneNumbers 接收者电话号码
     */
    @Override
    public void send(String content, Map<String, Object> parameters, String... phoneNumbers) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysAction(ACTION);
        request.setSysDomain(DOMAIN);
        request.setSysVersion(VERSION);
        request.putQueryParameter("PhoneNumbers", String.join(",", phoneNumbers));
        request.putQueryParameter("RegionId", REGION);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", content);
        if (parameters != null && parameters.size() > 0) {
            try {
                request.putQueryParameter("TemplateParam", objectMapper.writeValueAsString(parameters));
            } catch (JsonProcessingException e) {
                e.printStackTrace(System.err);
            }
        }
        try {
            client.getCommonResponse(request);
        } catch (ClientException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * 取得配置修订版本号。
     * @return 修订版本号
     */
    @Override
    public Long getRevision() {
        return revision;
    }
}
