package net.codelet.cloud.auth.command.service.impl;

import net.codelet.cloud.auth.command.dto.AuthenticateDTO;
import net.codelet.cloud.auth.command.entity.CredentialPasswordEntity;
import net.codelet.cloud.auth.command.repository.CredentialPasswordRepository;
import net.codelet.cloud.auth.command.service.AuthenticationService;
import net.codelet.cloud.error.AuthenticationError;
import net.codelet.cloud.user.query.api.UserQueryApi;
import net.codelet.cloud.user.query.entity.UserQueryEntity;
import net.codelet.cloud.util.PasswordUtils;
import net.codelet.cloud.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationServiceImpl implements AuthenticationService {

    private final CredentialPasswordRepository credentialPasswordRepository;

    private final UserQueryApi userQueryApi;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AuthenticationServiceImpl(
        CredentialPasswordRepository credentialPasswordRepository,
        UserQueryApi userQueryApi
    ) {
        this.credentialPasswordRepository = credentialPasswordRepository;
        this.userQueryApi = userQueryApi;
    }

    /**
     * 验证登录凭证，生成访问令牌。
     * @param authenticateDTO 登录凭证数据
     * @return 用户基本信息
     */
    @Override
    public UserQueryEntity authenticate(final AuthenticateDTO authenticateDTO) {
        // 尝试取得凭证信息
        CredentialPasswordEntity credentialEntity = credentialPasswordRepository
            .findByCredential(authenticateDTO.getUsername())
            .orElse(null);

        // 若凭证无效或密码不正确则返回认证错误
        if (credentialEntity == null
            || (!StringUtils.isEmpty(authenticateDTO.getPassword())
                && !PasswordUtils.validatePassword(authenticateDTO.getPassword(), credentialEntity.getPassword()))) {
            throw new AuthenticationError();
        }

        return userQueryApi.get(credentialEntity.getUserId());
    }
}
