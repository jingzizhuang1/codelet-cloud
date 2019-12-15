package net.codelet.cloud.constant;

/**
 * HTTP 请求头。
 */
public interface HttpRequestHeaders {

    /** 是否设置关联实体数据 */
    String SET_REFERENCED_ENTITIES = "X-Set-Referenced-Entities";

    /** 是否设置关联实体数据：否 */
    String SET_REFERENCED_ENTITIES_OFF = "Off";

    /** 客户端是否为 FeignClient */
    String IS_FEIGN_CLIENT = "X-Is-Feign-Client";

    /** 客户端是否为 FeignClient：是 */
    String IS_FEIGN_CLIENT_TRUE = "True";

    /** 用于内网访问的用户代理字符串 */
    String USER_AGENT_INTERNAL = "CodeletCloud/0.0.1 LAN";

    /** 内部调用访问令牌。 */
    String ACCESS_TOKEN = "X-Access-Token";
}
