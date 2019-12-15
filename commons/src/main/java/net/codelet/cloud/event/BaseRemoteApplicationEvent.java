package net.codelet.cloud.event;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * 远程应用事件基类。
 */
public abstract class BaseRemoteApplicationEvent<P> extends RemoteApplicationEvent {

    private static final long serialVersionUID = -3893525659551665140L;

    private static final ObjectMapper objectMapper = (new ObjectMapper())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * 事件携带的数据。
     */
    @Getter
    private P payload;

    /**
     * 附加数据，需要由事件消费者通过下一个事件回传，以维持事件源的上下文。
     */
    @Getter
    private Object additional = null;

    /**
     * 构造方法。
     */
    public BaseRemoteApplicationEvent() {
    }

    /**
     * 构造方法。
     * @param properties Spring Cloud Bus 配置属性对象
     * @param source     事件源对象
     */
    public BaseRemoteApplicationEvent(BusProperties properties, Object source) {
        this(properties, source, null, null, null);
    }

    /**
     * 构造方法。
     * @param properties Spring Cloud Bus 配置属性对象
     * @param source     事件源对象
     * @param payload    事件数据
     */
    public BaseRemoteApplicationEvent(BusProperties properties, Object source, P payload) {
        this(properties, source, null, payload, null);
    }

    /**
     * 构造方法。
     * @param properties Spring Cloud Bus 配置属性对象
     * @param source     事件源对象
     * @param payload    事件数据
     * @param additional 附加数据
     */
    public BaseRemoteApplicationEvent(BusProperties properties, Object source, P payload, Object additional) {
        this(properties, source, null, payload, additional);
    }

    /**
     * 构造方法。
     * @param properties         Spring Cloud Bus 配置属性对象
     * @param source             事件源对象
     * @param destinationService 目标服务
     * @param payload            事件数据
     */
    public BaseRemoteApplicationEvent(BusProperties properties, Object source, String destinationService, P payload) {
        this(properties, source, destinationService, payload, null);
    }

    /**
     * 构造方法。
     * @param properties         Spring Cloud Bus 配置属性对象
     * @param source             事件源对象
     * @param destinationService 目标服务
     * @param payload            事件数据
     * @param additional         附加数据
     */
    public BaseRemoteApplicationEvent(BusProperties properties, Object source, String destinationService, P payload, Object additional) {
        super(source, properties.getId(), destinationService);
        this.payload = payload;
        this.additional = additional;
    }

    /**
     * 取得附加数据并转为指定的类型。
     * @param type 附加数据类型
     * @param <A>  附加数据类型的范型
     * @return 附加数据
     */
    public <A> A getAdditional(Class<A> type) {
        if (additional == null) {
            return null;
        }
        return objectMapper.convertValue(additional, type);
    }
}
