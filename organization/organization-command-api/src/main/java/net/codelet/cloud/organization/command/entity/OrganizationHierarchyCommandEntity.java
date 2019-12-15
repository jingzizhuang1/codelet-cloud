package net.codelet.cloud.organization.command.entity;

import net.codelet.cloud.organization.entity.OrganizationHierarchyBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "organization_hierarchy")
public class OrganizationHierarchyCommandEntity extends OrganizationHierarchyBaseEntity {

    private static final long serialVersionUID = 159988433239355L;

}
