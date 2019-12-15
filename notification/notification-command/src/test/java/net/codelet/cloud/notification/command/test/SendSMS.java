package net.codelet.cloud.notification.command.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 短信发送测试。
 * @link https://help.aliyun.com/document_detail/56189.html?spm=a2c4g.11186623.6.660.710650a4OJ5EBS
 * @link https://www.showdoc.cc/1619992?page_id=14891022
 */
public class SendSMS {

    /**
     * 短信发送配置。
     */
    private static class SmsConfiguration {

        private static ObjectMapper objectMapper = (new ObjectMapper())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        @Getter
        @Setter
        @ApiModelProperty("配置名称")
        private String name;

        @Getter
        @ApiModelProperty("时间戳格式，默认为 ISO 时间（格式为 yyyy-MM-dd'T'HH:mm:ss.SSS'Z'，值如 2019-11-08T09:33:25.093Z）")
        private String timestampFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

        @Getter
        @Setter
        @NotBlank
        @Pattern(regexp = "^(GET|POST)$")
        @ApiModelProperty("请求方法")
        private String method;

        @Getter
        @Setter
        @NotBlank
        @ApiModelProperty("短信网关 URL")
        private String url;

        @Getter
        @Setter
        @ApiModelProperty("查询参数")
        private Map<String, String> searchParameters = new TreeMap<>();

        @ApiModelProperty(hidden = true)
        private String queryString;

        @Getter
        @Setter
        @ApiModelProperty("请求数据模版")
        private Map<String, String> body = new TreeMap<>();

        @ApiModelProperty(hidden = true)
        private String bodyJson;

        @Getter
        @Setter
        @ApiModelProperty("电话号码分隔符")
        private String phoneNumberSeparator = ",";

        @Getter
        @Setter
        @ApiModelProperty("签名算法")
        private String signAlgorithm;

        @Getter
        @Setter
        @ApiModelProperty("签名密钥")
        private String secret;

        @Getter
        @Setter
        @ApiModelProperty("签名参数名")
        private String signatureParamName;

        @JsonIgnore
        public String formatDate(Date date) {
            if (date == null) {
                return "";
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(timestampFormat);
            dateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
            return dateFormat.format(date);
        }

        /**
         * 取得查询字符串。
         * @return 查询字符串
         */
        public String getQueryString() {
            if (queryString != null) {
                return queryString;
            }
            Iterator<String> searchParameterNames = searchParameters.keySet().iterator();
            List<String> query = new ArrayList<>();
            while (searchParameterNames.hasNext()) {
                String searchParameterName = searchParameterNames.next();
                String searchParameterValue = searchParameters.get(searchParameterName);
                query.add(String.format(
                    "%s=%s",
                    encodeURIComponent(searchParameterName),
                    searchParameterValue.matches("\\$\\{.+?}")
                        ? searchParameterValue
                        : encodeURIComponent(searchParameterValue)
                ));
            }
            queryString = String.join("&", query);
            return queryString;
        }

        /**
         * 生成查询字符串。
         * @param content      短信内容
         * @param parameters   短信内容参数
         * @param phoneNumbers 电话号码数组
         * @return 查询字符串
         */
        public String getQueryString(String content, String parameters, String... phoneNumbers) {
            return getQueryString()
                .replace("${content}", encodeURIComponent(content))
                .replace("${parameters}", encodeURIComponent(parameters))
                .replace("${timestamp}", encodeURIComponent(formatDate(new Date())))
                .replace("${signatureNonce}", encodeURIComponent(UUID.randomUUID().toString()))
                .replace("${phoneNumbers}", encodeURIComponent(String.join(phoneNumberSeparator, phoneNumbers)));
        }

        /**
         * 生成请求数据 JSON。
         * @return 请求数据 JSON
         */
        public String getBodyJSON() {
            if (bodyJson != null) {
                return bodyJson;
            }
            try {
                bodyJson = objectMapper.writeValueAsString(body);
            } catch (JsonProcessingException e) {
                e.printStackTrace(System.err);
                bodyJson = "{}";
            }
            return bodyJson;
        }

        /**
         * 生成请求数据 JSON。
         * @param content      短信内容
         * @param parameters   短信内容参数
         * @param phoneNumbers 电话号码数组
         * @return 请求数据 JSON
         */
        public String getBodyJSON(String content, String parameters, String... phoneNumbers) {
            Iterator<String> propertyNames = body.keySet().iterator();
            Map<String, String> data = new TreeMap<>();
            while (propertyNames.hasNext()) {
                String propertyName = propertyNames.next();
                String propertyValue = body.get(propertyName);
                data.put(
                    propertyName,
                    propertyValue
                        .replace("${content}", content)
                        .replace("${parameters}", parameters != null ? parameters : "")
                        .replace("${timestamp}", formatDate(new Date()))
                        .replace("${signatureNonce}", UUID.randomUUID().toString())
                        .replace("${phoneNumbers}", String.join(phoneNumberSeparator, phoneNumbers))
                );
            }
            try {
                return objectMapper.writeValueAsString(data);
            } catch (JsonProcessingException e) {
                e.printStackTrace(System.err);
                return "{}";
            }
        }
    }

    /**
     * 发送短信。
     * @param configuration 短信配置
     * @param content       短信内容模版
     * @param parameters    短信内容模版参数
     * @param phoneNumbers  电话号码数组
     */
    private static void sendSMS(SmsConfiguration configuration, String content, String parameters, String... phoneNumbers) {
        String queryString = configuration.getQueryString(content, parameters, phoneNumbers);
        String signatureString = String.format("%s&%s&%s", configuration.getMethod(), encodeURIComponent("/"), encodeURIComponent(queryString));
        String signature = encodeURIComponent(sign(configuration.getSignAlgorithm(), signatureString, configuration.getSecret()));
        String url = configuration.getSignatureParamName() != null
            ? String.format("%s?%s=%s&%s", configuration.getUrl(), configuration.getSignatureParamName(), signature, queryString)
            : (queryString != null && !"".equals(queryString) ? String.format("%s?%s", configuration.getUrl(), queryString) : configuration.getUrl());
        String bodyJSON = configuration.getBodyJSON(content, parameters, phoneNumbers);
        try {
            System.out.println(String.format("\u001B[1;97;103m     Query String : %s \u001B[0m\n", queryString));
            System.out.println(String.format("\u001B[1;97;103m Signature String : %s \u001B[0m\n", signatureString));
            System.out.println(String.format("\u001B[1;97;106m              URL : %s \u001B[0m\n", url));
            System.out.println(String.format("\u001B[1;97;106m        Body JSON : %s \u001B[0m\n", bodyJSON));
            CloseableHttpResponse response;
            if ("GET".equals(configuration.method)) {
                response = HttpClients.createDefault().execute(new HttpGet(url));
            } else {
                HttpPost post = new HttpPost(url);
                post.setHeader("Content-Type", "application/json;charset=UTF-8");
                post.setEntity(new StringEntity(bodyJSON));
                response = HttpClients.createDefault().execute(post);
            }
            System.out.println(String.format("\u001B[1;97;101m         Response : %s \u001B[0m\n", EntityUtils.toString(response.getEntity())));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * 对 URL 中的字符串进行编码。
     * @param text 进行编码的字符串
     * @return 编码后的字符串
     */
    private static String encodeURIComponent(String text) {
        if (text == null) {
            return "";
        }
        try {
            return URLEncoder
                .encode(text, StandardCharsets.UTF_8.name())
                .replace("+", "%20")
                .replace("*", "%2A")
                .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 生成签名字符串。
     * @param algorithm    签名算法
     * @param stringToSign 待签名字符串
     * @param secret       签名密钥
     * @return 签名字符串
     */
    private static String sign(String algorithm, String stringToSign, String secret) {
        if (algorithm == null || stringToSign == null || secret == null) {
            return null;
        }
        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), algorithm));
            return DatatypeConverter.printBase64Binary(
                mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8))
            );
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * 通过阿里云提供的服务发送短信。
     * 用于测试通过 GET 方法及查询参数传递参数的短信发送方式。
     */
    @Test
    public void sendSmsByAliYun() {
        SmsConfiguration configuration = new SmsConfiguration();
        configuration.setName("阿里云短信网关");
        configuration.setMethod("GET");
        configuration.setUrl("http://dysmsapi.aliyuncs.com/");
        configuration.searchParameters.put("AccessKeyId", "eGh5prpCWn586w2X");
        configuration.searchParameters.put("Action", "SendSms");
        configuration.searchParameters.put("Format", "JSON");
        configuration.searchParameters.put("PhoneNumbers", "${phoneNumbers}");
        configuration.searchParameters.put("Region", "default");
        configuration.searchParameters.put("SecureTransport", "true");
        configuration.searchParameters.put("SignName", "Proding");
        configuration.searchParameters.put("SignatureMethod", "HMAC-SHA1");
        configuration.searchParameters.put("SignatureVersion", "1.0");
        configuration.searchParameters.put("TemplateCode", "${content}");
        configuration.searchParameters.put("TemplateParam", "${parameters}");
        configuration.searchParameters.put("Version", "2017-05-25");
        configuration.searchParameters.put("Timestamp", "${timestamp}");
        configuration.searchParameters.put("SignatureNonce", "${signatureNonce}");
        configuration.setSignAlgorithm("HmacSHA1");
        configuration.setSecret("IKquBzz8htlS1RH20zFhkYqk7mlZCa&");
        configuration.setSignatureParamName("Signature");
        sendSMS(configuration, "SMS_151232215", "{\"code\":\"123456\"}", "13130440737");
    }

    /**
     * 通过 ShowDoc 提供的服务发送短信。
     * 用于测试通过 POST 方法及请求数据传递参数的短信发送方式。
     */
    @Test
    public void sendSmsByShowDoc() {
        SmsConfiguration configuration = new SmsConfiguration();
        configuration.setName("ShowDoc短信网关");
        configuration.setMethod("POST");
        configuration.setUrl("http://showdoc.cc/");
        configuration.body.put("account", "N6000001");
        configuration.body.put("password", "123456");
        configuration.body.put("msg", "${content}");
        configuration.body.put("params", "${phoneNumbers}");
        configuration.setPhoneNumberSeparator(";");
        sendSMS(configuration, "【Proding】短信验证码是 123456", "{\"code\":\"123456\"}", "13130440737");
    }
}
