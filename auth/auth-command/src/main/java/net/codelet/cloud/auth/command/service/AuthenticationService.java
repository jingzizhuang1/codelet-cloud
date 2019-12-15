package net.codelet.cloud.auth.command.service;

import net.codelet.cloud.auth.command.dto.AuthenticateDTO;
import net.codelet.cloud.user.query.entity.UserQueryEntity;

public interface AuthenticationService {

    /**
     * 验证登录凭证，生成访问令牌。
     * @param authenticateDTO 登录凭证数据
     * @return 用户基本信息
     */
    UserQueryEntity authenticate(AuthenticateDTO authenticateDTO);
}
