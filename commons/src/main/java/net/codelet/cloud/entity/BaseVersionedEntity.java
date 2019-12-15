package net.codelet.cloud.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.ReferenceEntity;

import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 进行版本控制的实体的基类。
 */
@MappedSuperclass
public class BaseVersionedEntity extends BaseEntity {

    private static final long serialVersionUID = -2077299810336110174L;

    @Getter
    @Setter
    @ApiModelProperty("修订版本号（乐观锁）")
    private Long revision;

    @Getter
    @Setter
    @ApiModelProperty("创建时间")
    private Date createdAt;

    @Getter
    @Setter
    @ReferenceEntity("user")
    @ApiModelProperty("创建者 ID")
    private String createdBy;

    @Getter
    @Setter
    @ApiModelProperty("最后更新时间")
    private Date lastModifiedAt;

    @Getter
    @Setter
    @ReferenceEntity("user")
    @ApiModelProperty("最后更新者 ID")
    private String lastModifiedBy;

    @Getter
    @Setter
    @ApiModelProperty("是否已删除")
    private Boolean deleted = false;

    @Getter
    @Setter
    @ApiModelProperty("删除时间")
    private Date deletedAt;

    @Getter
    @Setter
    @ReferenceEntity("user")
    @ApiModelProperty("删除者 ID")
    private String deletedBy;

    /**
     * 刷新更新版本号。
     */
    public void updateRevision() {
        setRevision(System.currentTimeMillis());
    }
}
