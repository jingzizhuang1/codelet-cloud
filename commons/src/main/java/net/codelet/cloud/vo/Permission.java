package net.codelet.cloud.vo;

/**
 * 访问许可。
 */
public enum Permission {
    ALL(15),     // 全部：可以对作用领域内的数据执行所有操作
    GRANT(7),    // 授权：可以查看和更新作用领域内的数据，并且可以执行授权操作，不可创建、不可删除
    UPDATE(3),   // 更新：可以查看和更新作用领域内的数据，不可创建、不可删除
    RETRIEVE(1); // 查询：仅可查看作用领域内的数据

    private int weight;

    Permission(int weight) {
        this.weight = weight;
    }

    /**
     * 是否涵盖了指定的许可。
     * @param permission 比较对象
     * @return 比较结果
     */
    public boolean covers(Permission permission) {
        return this.weight >= permission.weight;
    }
}
