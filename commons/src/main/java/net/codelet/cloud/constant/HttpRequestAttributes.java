package net.codelet.cloud.constant;

import net.codelet.cloud.aspect.SetReferencedEntitiesAspect;

/**
 * HTTP 请求属性名。
 */
public interface HttpRequestAttributes {

    /** 请求上下文对象 */
    String CONTEXT = "net.codelet.cloud.RequestAttributes.CONTEXT";

    /** 客户端 IP 地址 */
    String REAL_IP = "net.codelet.cloud.RequestAttributes.REAL_IP";

    /** 代理服务器地址列表 */
    String FORWARDED_FOR = "net.codelet.cloud.RequestAttributes.FORWARDED_FOR";

    /** 返回结果关联实体缓存（通过 {@link SetReferencedEntitiesAspect} 设置） */
    String REFERENCED_ENTITIES = "net.codelet.cloud.RequestAttributes.REFERENCED_ENTITIES";
}
