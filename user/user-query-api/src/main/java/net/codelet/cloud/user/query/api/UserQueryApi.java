package net.codelet.cloud.user.query.api;

import net.codelet.cloud.api.BaseQueryApi;
import net.codelet.cloud.user.query.dto.UserQueryDTO;
import net.codelet.cloud.user.query.entity.UserQueryEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * 用户查询 REST 接口。
 */
@FeignClient(
    name = "${services.user.query.name:user-query}",
    contextId = "user-query"
)
public interface UserQueryApi extends BaseQueryApi<UserQueryEntity> {

    /**
     * 取得登录用户信息。
     * @return 登录用户信息
     */
    @GetMapping("/user")
    UserQueryEntity get();

    /**
     * 取得用户基本信息。
     * @param userId 用户 ID
     * @return 用户基本信息
     */
    @GetMapping("/users/{userId}")
    UserQueryEntity get(
        @PathVariable("userId") String userId
    );

    /**
     * 查询用户。
     * @param queryDTO 用户查询条件
     * @return 用户列表
     */
    @GetMapping("/users")
    Page<UserQueryEntity> search(
        UserQueryDTO queryDTO
    );

    /**
     * 批量取得用户信息。
     * @param entityIDs 实体 ID 集合。
     * @return 用户实体列表
     */
    @Override
    @PostMapping("/batch-get-users")
    List<UserQueryEntity> batchGet(@RequestParam("entityIDs") Set<String> entityIDs);
}
