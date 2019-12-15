package net.codelet.cloud.user.query.service;

import net.codelet.cloud.user.query.dto.UserQueryDTO;
import net.codelet.cloud.user.query.entity.UserQueryEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface UserQueryService {

    /**
     * 取得用户基本信息。
     * @param userId 用户 ID
     * @return 用户基本信息
     */
    UserQueryEntity get(String userId);

    /**
     * 批量取得用户信息。
     * @param userIDs 用户 ID 集合
     * @return 用户 ID 列表
     */
    List<UserQueryEntity> get(Set<String> userIDs);

    /**
     * 查询用户信息。
     * @param queryDTO 查询条件
     * @return 用户分页数据
     */
    Page<UserQueryEntity> search(UserQueryDTO queryDTO);
}
