package net.codelet.cloud.parameter.query.entity;

import net.codelet.cloud.parameter.entity.ParameterBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "parameter")
public class ParameterQueryEntity extends ParameterBaseEntity {
    private static final long serialVersionUID = 129270682446398L;
}
