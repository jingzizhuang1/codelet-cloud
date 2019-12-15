package net.codelet.cloud.auth.command.repository;

import net.codelet.cloud.auth.command.entity.UserPasswordCommandEntity;
import net.codelet.cloud.repository.BaseRepository;

import java.util.Optional;

public interface UserPasswordCommandRepository extends BaseRepository<UserPasswordCommandEntity> {

    /**
     * 取得登录密码数据。
     * @param userId 所有者用户 ID
     * @return 登录密码数据
     */
    Optional<UserPasswordCommandEntity> findByUserIdAndDeletedIsFalse(String userId);

}
