package net.codelet.cloud.organization.query.entity;

import net.codelet.cloud.organization.entity.OrganizationHierarchyBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "organization_hierarchy")
public class OrganizationHierarchyQueryEntity extends OrganizationHierarchyBaseEntity {

    private static final long serialVersionUID = 35657879535536L;

}
