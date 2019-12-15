package net.codelet.cloud.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class EntityReference implements Serializable {

    private static final long serialVersionUID = -7376014951813437714L;

    @ApiModelProperty(value = "引用目标数据实体 ID", name = "$ref")
    private String id;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String entityType;

    /**
     * 默认构造方法。
     */
    public EntityReference() {
    }

    /**
     * 构造方法。
     * @param id         引用数据实体的 ID
     * @param entityType 实体类型
     */
    private EntityReference(final String id, final String entityType) {
        this.id = id;
        this.entityType = entityType;
    }

    /**
     * 生成引用数据对象。
     * @param id         引用数据实体的 ID
     * @param entityType 实体类型
     * @return 引用数据对象
     */
    public static EntityReference instance(final String id, final String entityType) {
        return id == null ? null : new EntityReference(id, entityType);
    }

    /**
     * 取得引用数据对象的 ID。
     * @param reference 引用数据对象
     * @return 引用数据对象的 ID
     */
    public static String getId(EntityReference reference) {
        return reference == null ? null : reference.getId();
    }

    @JsonProperty("$ref")
    public String getId() {
        return id;
    }

    public String getEntityType() {
        return entityType;
    }
}
