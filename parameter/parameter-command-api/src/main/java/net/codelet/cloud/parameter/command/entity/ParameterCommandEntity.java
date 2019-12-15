package net.codelet.cloud.parameter.command.entity;

import net.codelet.cloud.parameter.entity.ParameterBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "parameter")
public class ParameterCommandEntity extends ParameterBaseEntity {
    private static final long serialVersionUID = 221075887341700L;
}
