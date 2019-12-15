package net.codelet.cloud.event;

import net.codelet.cloud.error.InternalServerError;
import net.codelet.cloud.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 事件发布器。
 */
@Component
public class EventPublisher {

    private final BusProperties properties;
    private final ApplicationEventPublisher publisher;

    /**
     * 构造方法。
     * @param properties Spring Cloud Bus 配置属性
     * @param publisher  事件发布器
     */
    @Autowired
    public EventPublisher(
        BusProperties properties,
        ApplicationEventPublisher publisher
    ) {
        this.properties = properties;
        this.publisher = publisher;
    }

    /**
     * 发布事件（不指定目标服务）。
     * @param source  触发事件的对象
     * @param type    事件的类型
     * @param <P>     携带数据的范型
     * @param <E>     事件的范型
     */
    public <P, E extends BaseRemoteApplicationEvent<P>> void publish(Object source, Class<E> type) {
        publish(source, type, null, null, null);
    }

    /**
     * 发布事件（不指定目标服务）。
     * @param source  触发事件的对象
     * @param type    事件的类型
     * @param payload 事件携带的数据
     * @param <P>     携带数据的范型
     * @param <E>     事件的范型
     */
    public <P, E extends BaseRemoteApplicationEvent<P>> void publish(Object source, Class<E> type, P payload) {
        publish(source, type, null, payload, null);
    }

    /**
     * 发布事件（不指定目标服务）。
     * @param source     触发事件的对象
     * @param type       事件的类型
     * @param payload    事件携带的数据
     * @param additional 附加数据（需要由事件消费者通过下一个事件回传）
     * @param <P>        携带数据的范型
     * @param <E>        事件的范型
     */
    public <P, E extends BaseRemoteApplicationEvent<P>> void publish(Object source, Class<E> type, P payload, Object additional) {
        publish(source, type, null, payload, additional);
    }

    /**
     * 发布事件（指定目标服务）。
     * @param source      触发事件的对象
     * @param type        事件的类型
     * @param sourceEvent 事件源事件
     * @param payload     事件携带的数据
     * @param <PE>        事件携带数据的范型
     * @param <E>         事件的范型
     * @param <PS>        事件源事件携带数据的范型
     * @param <S>         事件源事件的范型
     */
    public <PE, E extends BaseRemoteApplicationEvent<PE>, PS, S extends BaseRemoteApplicationEvent<PS>> void publish(Object source, Class<E> type, S sourceEvent, PE payload) {
        publish(source, type, sourceEvent.getOriginService(), payload, sourceEvent.getAdditional());
    }

    /**
     * 发布事件（指定目标服务）。
     * @param source      触发事件的对象
     * @param type        事件的类型
     * @param destination 目标服务 ID
     * @param payload     事件携带的数据
     * @param <P>         携带数据的范型
     * @param <E>         事件的范型
     */
    public <P, E extends BaseRemoteApplicationEvent<P>> void publish(Object source, Class<E> type, String destination, P payload) {
        publish(source, type, destination, payload, null);
    }

    /**
     * 发布事件（指定目标服务）。
     * @param source      触发事件的对象
     * @param type        事件的类型
     * @param destination 目标服务 ID
     * @param payload     事件携带的数据
     * @param additional  附加数据（需要由事件消费者通过下一个事件回传）
     * @param <P>         携带数据的范型
     * @param <E>         事件的范型
     */
    public <P, E extends BaseRemoteApplicationEvent<P>> void publish(Object source, Class<E> type, String destination, P payload, Object additional) {
        try {
            E event = type.newInstance();
            ReflectionUtils.set(event, "originService", properties.getId());
            ReflectionUtils.set(event, "source", source);
            if (payload != null) {
                ReflectionUtils.set(event, "payload", payload);
            }
            if (additional != null) {
                ReflectionUtils.set(event, "additional", additional);
            }
            if (destination != null) {
                ReflectionUtils.set(event, "destinationService", destination);
            }
            publisher.publishEvent(event);
        } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace(System.err);
            throw new InternalServerError();
        }
    }
}
