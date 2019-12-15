package net.codelet.cloud.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.util.CryptoUtils;
import net.codelet.cloud.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 数据实体基类。
 */
@MappedSuperclass
public class BaseEntity extends BaseDTO {

    private static final long serialVersionUID = -8267533702681020998L;

    @Id
    @Getter
    @ApiModelProperty("实体 ID")
    @Column(length = 16)
    private String id;

    public BaseEntity() {
        setId(null);
    }

    public void setId(String id) {
        if (StringUtils.isEmpty(id)) {
            this.id = CryptoUtils.uniqueId().toUpperCase();
        } else {
            this.id = id;
        }
    }
}
