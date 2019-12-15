-- -----------------------------------------------------------------------------
-- Codelet Cloud 数据库表创建脚本
-- 数据库：MySQL Server 5.7
-- 模　块：职员
-- -----------------------------------------------------------------------------
USE `codelet_cloud_employee`;

DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
    `id`               VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `company_id`       VARCHAR(16)  NOT NULL                  COMMENT '公司 ID',
    `user_id`          VARCHAR(16)  NOT NULL                  COMMENT '用户 ID',
    `employee_no`      VARCHAR(45)  DEFAULT NULL              COMMENT '员工号',
    `revision`         DOUBLE       NOT NULL                  COMMENT '修订版本号',
    `created_at`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`       VARCHAR(16)  NOT NULL                  COMMENT '创建者用户 ID',
    `approved`         BIT(1)       DEFAULT NULL              COMMENT '是否已通过',
    `approved_at`      DATETIME     DEFAULT NULL              COMMENT '通过时间',
    `approved_by`      VARCHAR(16)  DEFAULT NULL              COMMENT '通过操作者用户 ID',
    `rejected_at`      DATETIME     DEFAULT NULL              COMMENT '拒绝时间',
    `rejected_by`      VARCHAR(16)  DEFAULT NULL              COMMENT '拒绝操作者用户 ID',
    `comment`          VARCHAR(255) DEFAULT NULL              COMMENT '批注',
    `last_modified_at` DATETIME     DEFAULT NULL              COMMENT '最后更新时间',
    `last_modified_by` VARCHAR(16)  DEFAULT NULL              COMMENT '最后更新者用户 ID',
    `deleted`          BIT(1)       DEFAULT 0                 COMMENT '是否已删除',
    `deleted_at`       DATETIME     DEFAULT NULL              COMMENT '删除时间',
    `deleted_by`       VARCHAR(16)  DEFAULT NULL              COMMENT '删除操作者用户 ID',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '公司职员';
