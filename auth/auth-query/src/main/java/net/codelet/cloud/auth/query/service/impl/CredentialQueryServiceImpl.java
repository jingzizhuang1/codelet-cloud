package net.codelet.cloud.auth.query.service.impl;

import net.codelet.cloud.auth.dto.CredentialDTO;
import net.codelet.cloud.auth.query.repository.CredentialQueryRepository;
import net.codelet.cloud.auth.query.service.CredentialQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 认证凭证查询服务。
 */
@Component
public class CredentialQueryServiceImpl implements CredentialQueryService {

    private final CredentialQueryRepository credentialQueryRepository;

    @Autowired
    public CredentialQueryServiceImpl(CredentialQueryRepository credentialQueryRepository) {
        this.credentialQueryRepository = credentialQueryRepository;
    }

    /**
     * 检查认证凭证是否存在。
     * @param credential 认证凭证
     * @return 检查结果
     */
    @Override
    public boolean exists(String credential) {
        CredentialDTO dto = new CredentialDTO(credential);
        return credentialQueryRepository
            .existsByTypeAndCredentialAndDeletedIsFalse(dto.getType(), dto.getCredential());
    }
}
