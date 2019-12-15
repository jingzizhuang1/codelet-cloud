package net.codelet.cloud.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.codelet.cloud.annotation.ReferenceEntity;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.dto.response.JsonApiErrorDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JacksonConfiguration {

    public static final String REFERENCE_PROPERTY_NAME = "$ref";

    /**
     * JSON 序列化配置。
     */
    @Bean
    public ObjectMapper objectMapper() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new ReferenceEntitySerializer());
        module.addDeserializer(String.class, new ReferenceEntityDeserializer());
        return (new ObjectMapper())
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(module)
            .addMixIn(JsonApiErrorDTO.class, JsonApiErrorMixin.class);
    }

    /**
     * 将实体 ID 属性序列化为 {"$ref":"entityId"} 的形式。
     * 实体 ID 属性需要通过 @ReferenceEntity 注解标注。
     */
    private static class ReferenceEntitySerializer extends JsonSerializer<String> {

        private static final Map<String, Boolean> cache = new HashMap<>();

        /**
         * 序列化方法。
         * @param entityId  实体 ID
         * @param generator JSON 生成器
         * @param provider  序列化工具 Provider
         * @throws IOException IO 异常
         */
        @Override
        public void serialize(String entityId, JsonGenerator generator, SerializerProvider provider) throws IOException {
            // 取得正被序列化的对象
            Object object = generator.getCurrentValue();

            // 取得正被序列化的对象的属性名
            String propertyName = generator.getOutputContext().getCurrentName();

            // 若正在序列化的属性不为关联实体的 ID（即未被 @ReferenceEntity 标注），则不进行处理
            if (!(object instanceof BaseDTO)
                || REFERENCE_PROPERTY_NAME.equals(propertyName)
                || !isReferenceEntityId(object.getClass(), propertyName)) {
                generator.writeString(entityId);
                return;
            }

            // 否则，将实体 ID 重新格式化为 {"$ref":"entityId"} 的形式
            generator.writeStartObject();
            generator.writeStringField(REFERENCE_PROPERTY_NAME, entityId);
            generator.writeEndObject();
        }

        /**
         * 检查将被序列化的属性是否拥有 @ReferenceEntity 注解。
         * @param type      属性所属的类型
         * @param fieldName 属性名称
         * @return 检查结果
         */
        private boolean isReferenceEntityId(Class<?> type, String fieldName) {
            if (type == null || fieldName == null) {
                return false;
            }
            final String cacheKey = type.getName() + "#" + fieldName;
            if (cache.containsKey(cacheKey)) {
                return cache.get(cacheKey);
            }
            if (type != BaseDTO.class && isReferenceEntityId(type.getSuperclass(), fieldName)) {
                cache.put(cacheKey, true);
                return true;
            }
            try {
                if (type.getDeclaredField(fieldName).getAnnotation(ReferenceEntity.class) != null) {
                    cache.put(cacheKey, true);
                    return true;
                }
            } catch (NoSuchFieldException ignored) {
                for (Field field : type.getDeclaredFields()) {
                    JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
                    if (jsonProperty != null && fieldName.equals(jsonProperty.value())) {
                        cache.put(cacheKey, true);
                        return true;
                    }
                }
            }
            cache.put(cacheKey, false);
            return false;
        }
    }

    /**
     * 将序列化的关联实体 ID 由 {"$ref":"entityId"} 的形式反序列化为字符串的形式。
     */
    private static class ReferenceEntityDeserializer extends JsonDeserializer<String> {

        /**
         * 反序列化方法。
         * @param parser  JSON Parser
         * @param context 反序列化上下文对象
         * @return 反序列化结果
         * @throws IOException IO 异常
         */
        @Override
        public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode node = parser.getCodec().readTree(parser);
            JsonNode entityId = node.get("$ref");
            return entityId == null ? node.textValue() : entityId.textValue();
        }
    }

    /**
     * 不返回错误详细信息到客户端。
     */
    private static abstract class JsonApiErrorMixin {
        @JsonIgnore public abstract Throwable getCause();
        @JsonIgnore public abstract StackTraceElement[] getStackTrace();
        @JsonIgnore public abstract String getLocalizedMessage();
        @JsonIgnore public abstract Throwable[] getSuppressed();
    }
}
