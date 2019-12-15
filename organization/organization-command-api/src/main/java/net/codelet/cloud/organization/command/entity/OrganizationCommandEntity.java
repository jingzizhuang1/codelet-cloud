package net.codelet.cloud.organization.command.entity;

import net.codelet.cloud.organization.entity.OrganizationBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "organization")
public class OrganizationCommandEntity extends OrganizationBaseEntity {

    private static final long serialVersionUID = 141156743266815L;

}
