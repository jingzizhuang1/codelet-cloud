package net.codelet.cloud.auth.command.repository;

import net.codelet.cloud.auth.command.entity.CredentialPasswordEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CredentialPasswordRepository extends CrudRepository<CredentialPasswordEntity, String> {

    /**
     * 检查用户登录凭证是否存在并取得登录密码数据。
     * @param credential 登录凭证
     * @return 登录凭证-密码视图数据
     */
    Optional<CredentialPasswordEntity> findByCredential(String credential);
}
