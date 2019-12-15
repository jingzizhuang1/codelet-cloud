package net.codelet.cloud.config;

import net.codelet.cloud.event.BaseBroadcastEvent;
import net.codelet.cloud.stream.BroadcastClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.autoconfigure.LifecycleMvcEndpointAutoConfiguration;
import org.springframework.cloud.bus.*;
import org.springframework.cloud.bus.endpoint.EnvironmentBusEndpoint;
import org.springframework.cloud.bus.endpoint.RefreshBusEndpoint;
import org.springframework.cloud.bus.event.*;
import org.springframework.cloud.context.environment.EnvironmentManager;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.config.BindingServiceConfiguration;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.PostConstruct;

/**
 * <p>事件总线配置。</p>
 * <p>广播及非广播事件分别使用不同的频道。</p>
 * <p>广播频道不分组，以使得同一业务的微服务实例都能够接收到事件。</p>
 * <p>非广播事件分组，以使得同一业务的微服务实例仅一个能够接收到事件，从而避免事件被重复消费。</p>
 * @see org.springframework.cloud.bus.BusAutoConfiguration
 */
@Configuration
@ConditionalOnBusEnabled
@EnableBinding({BroadcastClient.class, SpringCloudBusClient.class})
@EnableConfigurationProperties(BusProperties.class)
@AutoConfigureBefore(BindingServiceConfiguration.class)
@AutoConfigureAfter(LifecycleMvcEndpointAutoConfiguration.class)
public class EventBusConfiguration implements ApplicationEventPublisherAware {

    private final ServiceMatcher serviceMatcher;
    private final BindingServiceProperties bindings;
    private final BusProperties busProperties;
    private ApplicationEventPublisher applicationEventPublisher;

    // 用于发布事件的频道
    private MessageChannel eventBusOutputChannel;

    // 用于发布广播的频道
    private MessageChannel broadcastOutputChannel;

    /**
     * 构造方法。
     * @param serviceMatcher 用于判断服务是否匹配
     * @param bindings       用于将事件分发到不同的频道
     * @param busProperties  事件总线配置
     */
    @Autowired
    public EventBusConfiguration(
        ServiceMatcher serviceMatcher,
        BindingServiceProperties bindings,
        BusProperties busProperties
    ) {
        this.serviceMatcher = serviceMatcher;
        this.bindings = bindings;
        this.busProperties = busProperties;
    }

    /**
     * 初始化。
     */
    @PostConstruct
    public void init() {
        setBinding(SpringCloudBusClient.INPUT);
        setBinding(SpringCloudBusClient.OUTPUT);
        setBinding(BroadcastClient.INPUT);
        setBinding(BroadcastClient.OUTPUT);
    }

    /**
     * 设置绑定的频道。
     * @param bindingName 绑定名
     */
    private void setBinding(String bindingName) {
        BindingProperties properties = bindings.getBindings().get(bindingName);
        if (properties == null) {
            properties = new BindingProperties();
            bindings.getBindings().put(bindingName, properties);
        }
        if (properties.getDestination() == null || properties.getDestination().equals(bindingName)) {
            properties.setDestination(this.busProperties.getDestination());
        }
    }

    /**
     * 注入事件发布器实例。
     * @param applicationEventPublisher 事件发布器
     */
    @Override
    @SuppressWarnings("NullableProblems")
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 注入事件发布频道。
     * @param eventBusOutputChannel 事件发布频道
     */
    @Autowired
    @Output(SpringCloudBusClient.OUTPUT)
    public void setEventBusOutboundChannel(MessageChannel eventBusOutputChannel) {
        this.eventBusOutputChannel = eventBusOutputChannel;
    }

    /**
     * 监听本地非广播事件。
     * @param event 广播事件
     */
    @EventListener(classes = RemoteApplicationEvent.class)
    public void acceptLocalNonBroadcast(RemoteApplicationEvent event) {
        if (serviceMatcher.isFromSelf(event)
            && !(event instanceof AckRemoteApplicationEvent)
            && !(event instanceof BaseBroadcastEvent)) {
            eventBusOutputChannel.send(MessageBuilder.withPayload(event).build());
        }
    }

    /**
     * 监听远程非广播事件。
     * @param event 非广播事件
     */
    @StreamListener(SpringCloudBusClient.INPUT)
    public void acceptRemoteNonBroadcast(RemoteApplicationEvent event) {
        if (!(event instanceof BaseBroadcastEvent)) {
            acceptRemote(eventBusOutputChannel, event);
        }
    }

    /**
     * 注入广播发布频道。
     * @param broadcastOutputChannel 广播发布频道
     */
    @Autowired
    @Output(BroadcastClient.OUTPUT)
    public void setBroadcastOutboundChannel(MessageChannel broadcastOutputChannel) {
        this.broadcastOutputChannel = broadcastOutputChannel;
    }

    /**
     * 监听本地广播事件。
     * @param event 广播事件
     */
    @EventListener(classes = BaseBroadcastEvent.class)
    public void acceptLocalBroadcast(BaseBroadcastEvent event) {
        if (serviceMatcher.isFromSelf(event)) {
            broadcastOutputChannel.send(MessageBuilder.withPayload(event).build());
        }
    }

    /**
     * 监听远程广播事件。
     * @param event 广播事件
     */
    @StreamListener(BroadcastClient.INPUT)
    public void acceptRemoteBroadcast(RemoteApplicationEvent event) {
        acceptRemote(null, event);
    }

    /**
     * 接收远程事件，并将其发布到本地环境。
     * @param messageChannel 消息频道
     * @param event          远程事件
     */
    private void acceptRemote(MessageChannel messageChannel, RemoteApplicationEvent event) {
        if (applicationEventPublisher == null) {
            return;
        }
        // 若为远程应用反馈事件则将其发布到本地并结束
        if (event instanceof AckRemoteApplicationEvent) {
            if (busProperties.getTrace().isEnabled() && !serviceMatcher.isFromSelf(event)) {
                applicationEventPublisher.publishEvent(event);
            }
            return;
        }
        // 否则，若为发布到当前应用的事件……
        if (serviceMatcher.isForSelf(event)) {
            // ……且该事件来自其他应用，则将其发布到本地
            if (!serviceMatcher.isFromSelf(event)) {
                applicationEventPublisher.publishEvent(event);
            }
            // 并且，若开启了反馈，则发布反馈事件
            if (messageChannel != null && busProperties.getAck().isEnabled()) {
                AckRemoteApplicationEvent ack = new AckRemoteApplicationEvent(
                    this,
                    serviceMatcher.getServiceId(),
                    busProperties.getAck().getDestinationService(),
                    event.getDestinationService(), event.getId(), event.getClass()
                );
                messageChannel.send(MessageBuilder.withPayload(ack).build());
                applicationEventPublisher.publishEvent(ack);
            }
        }
        // 若开启了事件追踪则向本地发送事件已发送事件
        if (busProperties.getTrace().isEnabled()) {
            applicationEventPublisher.publishEvent(new SentApplicationEvent(
                this,
                event.getOriginService(),
                event.getDestinationService(),
                event.getId(),
                event.getClass()
            ));
        }
    }

    @Configuration
    protected static class MatcherConfiguration {
        @BusPathMatcher
        @ConditionalOnMissingBean(name = BusAutoConfiguration.BUS_PATH_MATCHER_NAME)
        @Bean(name = BusAutoConfiguration.BUS_PATH_MATCHER_NAME)
        public PathMatcher busPathMatcher() {
            return new DefaultBusPathMatcher(new AntPathMatcher(":"));
        }
        @Bean
        public ServiceMatcher serviceMatcher(@BusPathMatcher PathMatcher pathMatcher, BusProperties properties, Environment environment) {
            return new ServiceMatcher(
                pathMatcher,
                properties.getId(),
                environment.getProperty(BusAutoConfiguration.CLOUD_CONFIG_NAME_PROPERTY, String[].class, new String[] {})
            );
        }
    }

    @Bean
    @ConditionalOnProperty(value = "spring.cloud.bus.refresh.enabled", matchIfMissing = true)
    @ConditionalOnBean(ContextRefresher.class)
    public RefreshListener refreshListener(ContextRefresher contextRefresher) {
        return new RefreshListener(contextRefresher);
    }

    @Configuration
    @ConditionalOnClass({Endpoint.class, RefreshScope.class})
    protected static class BusRefreshConfiguration {
        @Configuration
        @ConditionalOnBean(ContextRefresher.class)
        protected static class BusRefreshEndpointConfiguration {
            @Bean
            @ConditionalOnEnabledEndpoint
            public RefreshBusEndpoint refreshBusEndpoint(ApplicationContext context, BusProperties bus) {
                return new RefreshBusEndpoint(context, bus.getId());
            }
        }
    }

    @Configuration
    @ConditionalOnClass({Endpoint.class})
    @ConditionalOnBean(HttpTraceRepository.class)
    @ConditionalOnProperty(value = "spring.cloud.bus.trace.enabled", matchIfMissing = false)
    protected static class BusAckTraceConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public TraceListener ackTraceListener(HttpTraceRepository repository) {
            return new TraceListener(repository);
        }
    }

    @Configuration
    @ConditionalOnClass(EnvironmentManager.class)
    @ConditionalOnBean(EnvironmentManager.class)
    protected static class BusEnvironmentConfiguration {
        @Bean
        @ConditionalOnProperty(value = "spring.cloud.bus.env.enabled", matchIfMissing = true)
        public EnvironmentChangeListener environmentChangeListener() {
            return new EnvironmentChangeListener();
        }
        @Configuration
        @ConditionalOnClass(Endpoint.class)
        protected static class EnvironmentBusEndpointConfiguration {
            @Bean
            @ConditionalOnEnabledEndpoint
            public EnvironmentBusEndpoint environmentBusEndpoint(ApplicationContext context, BusProperties bus) {
                return new EnvironmentBusEndpoint(context, bus.getId());
            }
        }
    }
}
