package net.codelet.cloud.api;

import net.codelet.cloud.entity.BaseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * 实体查询 API 基础接口。
 */
public interface BaseQueryApi<T extends BaseEntity> {

    /**
     * 根据实体 ID 的集合取得实体列表。
     * 务必重写此方法以设置请求路径。
     * @param entityIDs 实体 ID 集合。
     * @return 实体列表
     */
    @PostMapping("/batch-get-entities")
    List<T> batchGet(@RequestParam("entityIDs") Set<String> entityIDs);
}
