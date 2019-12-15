package net.codelet.cloud.employee.command.entity;

import net.codelet.cloud.employee.entity.EmployeeBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 公司职员。
 */
@Entity
@Table(name = "employee")
public class EmployeeCommandEntity extends EmployeeBaseEntity {
    private static final long serialVersionUID = 278096484698935L;
}
