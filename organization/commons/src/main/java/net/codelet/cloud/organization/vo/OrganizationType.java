package net.codelet.cloud.organization.vo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 组织类型。
 */
public enum OrganizationType {
    ROOT(), // 系统根节点
    COMPANY("ROOT"), // 公司
    DIVISION("COMPANY", "DIVISION"), // 事业部
    GROUP("COMPANY", "DIVISION", "GROUP"); // 工作组

    private Set<String> parentTypes = new HashSet<>();

    OrganizationType(String... parentTypeNames) {
        parentTypes.addAll(Arrays.asList(parentTypeNames));
    }

    public boolean canBeParentOf(OrganizationType childType) {
        return childType.parentTypes.contains(this.name());
    }
}
