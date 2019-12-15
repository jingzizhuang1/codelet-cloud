package net.codelet.cloud.auth.command.repository;

import net.codelet.cloud.repository.BaseRepository;
import net.codelet.cloud.auth.command.entity.AccessTokenCommandEntity;

import java.util.Optional;

public interface AccessTokenCommandRepository extends BaseRepository<AccessTokenCommandEntity> {

    Optional<AccessTokenCommandEntity> findByUserIdAndClient(String userId, String client);

    Optional<AccessTokenCommandEntity> findByIdAndUserIdAndClient(String id, String userId, String client);
}
