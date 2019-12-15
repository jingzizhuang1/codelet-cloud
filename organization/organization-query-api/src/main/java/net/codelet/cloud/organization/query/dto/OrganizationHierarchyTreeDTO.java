package net.codelet.cloud.organization.query.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.annotation.ReferenceEntity;
import net.codelet.cloud.config.JacksonConfiguration;
import net.codelet.cloud.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 组织层级结构数据传输对象。
 */
public class OrganizationHierarchyTreeDTO extends BaseDTO {

    private static final long serialVersionUID = -8133524036472181097L;

    @Getter
    @Setter
    @ReferenceEntity("organization")
    @JsonProperty(JacksonConfiguration.REFERENCE_PROPERTY_NAME)
    @ApiModelProperty("组织 ID")
    private String id;

    @Getter
    @Setter
    @ApiModelProperty("下级组织列表")
    private List<OrganizationHierarchyTreeDTO> children = null;

    public OrganizationHierarchyTreeDTO() {
    }

    public OrganizationHierarchyTreeDTO(String id) {
        setId(id);
    }

    public void addChild(OrganizationHierarchyTreeDTO child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }
}
