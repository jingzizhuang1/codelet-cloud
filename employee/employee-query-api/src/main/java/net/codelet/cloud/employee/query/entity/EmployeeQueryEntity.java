package net.codelet.cloud.employee.query.entity;

import net.codelet.cloud.employee.entity.EmployeeBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
public class EmployeeQueryEntity extends EmployeeBaseEntity {

    private static final long serialVersionUID = 228803531798798L;

}
