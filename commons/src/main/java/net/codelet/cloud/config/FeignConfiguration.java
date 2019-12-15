package net.codelet.cloud.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import net.codelet.cloud.dto.response.JsonApiListDTO;
import net.codelet.cloud.error.BaseError;
import net.codelet.cloud.error.FeignBadRequestError;
import net.codelet.cloud.util.RequestContextUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static net.codelet.cloud.constant.HttpRequestHeaders.*;
import static org.springframework.http.HttpHeaders.*;

/**
 * FeignClient 配置。
 */
@Configuration
public class FeignConfiguration {

    private static final ErrorDecoder DEFAULT_ERROR_DECODER = new ErrorDecoder.Default();
    private final Encoder defaultEncoder;
    private final ObjectMapper objectMapper;

    /**
     * 构造方法。
     * @param objectMapper        {@link JacksonConfiguration} 中定义的 ObjectMapper
     * @param messageConverters   消息转换器
     */
    @Autowired
    public FeignConfiguration(
        ObjectMapper objectMapper,
        ObjectFactory<HttpMessageConverters> messageConverters
    ) {
        this.objectMapper = objectMapper;
        this.defaultEncoder = new SpringEncoder(messageConverters);
    }

    /**
     * 拦截器，用于在发送请求前设置请求头，如客户端用户代理字符串、认证信息等。
     * @return RequestInterceptor
     */
    @Bean
    public RequestInterceptor interceptor() {
        return template -> {
            // 设置请求头
            template
                .header(IS_FEIGN_CLIENT, IS_FEIGN_CLIENT_TRUE) // 通过请求头将当前请求标记为 FeignClient
                .header(SET_REFERENCED_ENTITIES, SET_REFERENCED_ENTITIES_OFF); // 通过 FeignClient 调用接口时不设置关联实体数据

            // 取得 HTTP 请求实例
            HttpServletRequest request = RequestContextUtils.getRequest();

            // 保持认证信息
            if (request != null) {
                if (request.getHeader(USER_AGENT) != null) {
                    template.header(USER_AGENT, request.getHeader(USER_AGENT));
                }
                if (request.getHeader(AUTHORIZATION) != null) {
                    template.header(AUTHORIZATION, request.getHeader(AUTHORIZATION));
                }
                if (request.getAttribute(ACCESS_TOKEN) != null) {
                    template.header(ACCESS_TOKEN, (String) request.getAttribute(ACCESS_TOKEN));
                    request.removeAttribute(ACCESS_TOKEN);
                }
            // 设置默认认证信息
            } else {
                template.header(USER_AGENT, USER_AGENT_INTERNAL);
                template.header(AUTHORIZATION, "");
            }
        };
    }

    /**
     * 编码器，用于将 GET 方法的对象类型参数编码为查询参数。
     * <p>默认情况下，若请求方法为 GET 且存在类型为对象的参数，则默认的编码器将会把方法更改为 POST，并将对象类型的参数编码为请求数据（Body）。</p>
     * <p>为避免以上情况发生，重新实现了 Encoder 接口，并在发送 GET 请求时将对象类型的参数编码为查询参数（Query）。</p>
     * @return Encoder
     */
    @Bean
    public Encoder encoder() {
        return (object, type, template) -> {
            // 若不为 GET 方法则使用默认编码器
            if (!HttpMethod.GET.name().equals(template.method())) {
                defaultEncoder.encode(object, type, template);
                return;
            }

            // 取得对象的所有属性
            Map<String, Object> params = objectMapper.convertValue(
                object,
                objectMapper
                    .getTypeFactory()
                    .constructMapType(LinkedHashMap.class, String.class, Object.class)
            );

            // 将对象的属性作为请求的查询参数
            params.forEach((key, value) -> {
                if (value == null) {
                    return;
                }
                if (value instanceof Object[]) {
                    value = Arrays.asList((Object[]) value);
                }
                if (value instanceof Collection) {
                    List<String> items = new ArrayList<>();
                    for (Object item : (Collection) value) {
                        if (item == null) {
                            continue;
                        }
                        items.add(item.toString());
                    }
                    if (items.size() > 0) {
                        template.query(key, items.toArray(new String[] {}));
                    }
                } else {
                    template.query(key, value.toString());
                }
            });
        };
    }

    /**
     * 取得响应头数据。
     * @param response 响应
     * @param key      响应头 KEY
     * @return 响应头数据
     */
    private String getHeader(Response response, String key) {
        Collection<String> values = response.headers().get(key);
        if (values == null || values.size() == 0) {
            return null;
        }
        return values.iterator().next();
    }

    /**
     * 取得响应数据的媒体类型。
     * @param response 响应
     * @return 媒体类型
     */
    private MimeType getContentType(Response response) {
        String contentType = getHeader(response, CONTENT_TYPE);
        if (contentType == null) {
            return null;
        }
        return MimeTypeUtils.parseMimeType(contentType);
    }

    /**
     * 正常响应解码器。
     * @return Decoder
     */
    @Bean
    public Decoder decoder() {
        return (response, type) -> {
            if (response.body().length() == 0) {
                return null;
            }

            // 反序列化 JSON 数据
            LinkedHashMap result = (LinkedHashMap) objectMapper
                .readValue(response.body().asInputStream(), Object.class);

            // 取得响应数据（LinkedHashMap/List<LinkedHashMap>）
            Object data = result == null ? null : result.get("data");

            // 若为列表则将其转换为目标类型的列表
            if (data instanceof Collection) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<?> actualClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];

                // 接口方法的返回类型为 Page 时
                if (parameterizedType.getRawType() == Page.class) {
                    List content = objectMapper.convertValue(
                        data,
                        objectMapper.getTypeFactory().constructCollectionType(
                            List.class,
                            actualClass
                        )
                    );
                    JsonApiListDTO.Meta meta = objectMapper
                        .convertValue(result.get("meta"), JsonApiListDTO.Meta.class);
                    Pageable pageable = meta != null
                        ? PageRequest.of(meta.getPageNo() - 1, meta.getPageSize())
                        : PageRequest.of(0, 0);
                    // noinspection unchecked
                    return new PageImpl(content, pageable, meta != null ? meta.getCount() : content.size());
                }

                // 接口方法返回的类型为 List 或 Set 时
                // noinspection unchecked
                return objectMapper.convertValue(
                    data,
                    objectMapper.getTypeFactory().constructCollectionType(
                        (Class<? extends Collection>) parameterizedType.getRawType(),
                        actualClass
                    )
                );
            }

            // 否则转换为目标类型对象
            return objectMapper.convertValue(data, (Class<?>) type);
        };
    }

    /**
     * 错误响应数据解码器。
     * @return ErrorDecoder
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            // 若为返回数据或不为 JSON 数据则使用默认解码器
            if (response.body() == null
                || response.body().length() == 0
                || !MimeTypeUtils.APPLICATION_JSON.equalsTypeAndSubtype(getContentType(response))) {
                return DEFAULT_ERROR_DECODER.decode(methodKey, response);
            }

            LinkedHashMap result;
            LinkedHashMap error = null;
            Class<?> errorType = null;

            // 反序列化响应数据
            try {
                result = (LinkedHashMap) objectMapper.readValue(response.body().asInputStream(), Object.class);
                error = (LinkedHashMap) result.get("error");
                errorType = error == null ? null : Class.forName((String) error.get("type"));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace(System.err);
            }

            // 若无错误信息则使用默认解码器
            if (error == null || errorType == null) {
                return DEFAULT_ERROR_DECODER.decode(methodKey, response);
            }

            throw new FeignBadRequestError((BaseError) objectMapper.convertValue(error, errorType));
        };
    }
}
