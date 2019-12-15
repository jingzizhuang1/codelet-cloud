package net.codelet.cloud.user.query.service.impl;

import net.codelet.cloud.error.NotFoundError;
import net.codelet.cloud.user.query.dto.UserQueryDTO;
import net.codelet.cloud.user.query.entity.UserQueryEntity;
import net.codelet.cloud.user.query.repository.UserQueryRepository;
import net.codelet.cloud.user.query.service.UserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class UserQueryServiceImpl implements UserQueryService {

    private final UserQueryRepository userQueryRepository;

    @Autowired
    public UserQueryServiceImpl(UserQueryRepository userQueryRepository) {
        this.userQueryRepository = userQueryRepository;
    }

    /**
     * 取得用户基本信息。
     * @param userId 用户 ID
     * @return 用户基本信息
     */
    @Override
    public UserQueryEntity get(final String userId) {

        UserQueryEntity userEntity = userQueryRepository
            .findById(userId)
            .orElse(null);

        if (userEntity == null) {
            throw new NotFoundError();
        }

        return userEntity;
    }

    /**
     * 批量取得用户信息。
     * @param userIDs 用户 ID 集合
     * @return 用户 ID 列表
     */
    @Override
    public List<UserQueryEntity> get(Set<String> userIDs) {
        return userQueryRepository.findByIdIn(userIDs);
    }

    /**
     * 查询用户信息。
     * @param queryDTO 查询条件
     * @return 用户分页数据
     */
    @Override
    public Page<UserQueryEntity> search(UserQueryDTO queryDTO) {
        return userQueryRepository.findAllByCriteria(queryDTO, queryDTO.pageable());
    }
}
