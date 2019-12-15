package net.codelet.cloud.organization.query.entity;

import net.codelet.cloud.organization.entity.OrganizationBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "organization")
public class OrganizationQueryEntity extends OrganizationBaseEntity {

    private static final long serialVersionUID = 154664130552902L;

}
