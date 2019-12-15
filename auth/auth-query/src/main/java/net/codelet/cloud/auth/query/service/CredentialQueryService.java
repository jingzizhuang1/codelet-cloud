package net.codelet.cloud.auth.query.service;

/**
 * 认证凭证查询服务接口。
 */
public interface CredentialQueryService {

    /**
     * 检查认证凭证是否存在。
     * @param credential 认证凭证
     * @return 检查结果
     */
    boolean exists(String credential);
}
