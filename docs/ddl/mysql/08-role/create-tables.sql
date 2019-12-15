-- -----------------------------------------------------------------------------
-- Codelet Cloud 数据库表创建脚本
-- 数据库：MySQL Server 5.7
-- 模　块：角色
-- -----------------------------------------------------------------------------
USE `codelet_cloud_role`;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
    `id`               VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `company_id`       VARCHAR(16)  NOT NULL                  COMMENT '公司 ID',
    `org_id`           VARCHAR(16)  NOT NULL                  COMMENT '组织 ID',
    `name`             VARCHAR(16)  NOT NULL                  COMMENT '名称',
    `name_pinyin`      VARCHAR(45)  NOT NULL                  COMMENT '名称拼音',
    `revision`         DOUBLE       NOT NULL                  COMMENT '修订版本号',
    `created_at`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`       VARCHAR(16)  NOT NULL                  COMMENT '创建者用户 ID',
    `last_modified_at` DATETIME     DEFAULT NULL              COMMENT '最后更新时间',
    `last_modified_by` VARCHAR(16)  DEFAULT NULL              COMMENT '最后更新者用户 ID',
    `deleted`          BIT(1)       DEFAULT 0                 COMMENT '是否已删除',
    `deleted_at`       DATETIME     DEFAULT NULL              COMMENT '删除时间',
    `deleted_by`       VARCHAR(16)  DEFAULT NULL              COMMENT '删除操作者用户 ID',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '组织角色';

DROP TABLE IF EXISTS `role_privilege`;
CREATE TABLE `role_privilege` (
    `id`               VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `company_id`       VARCHAR(16)  NOT NULL                  COMMENT '公司 ID',
    `org_id`           VARCHAR(16)  NOT NULL                  COMMENT '组织 ID',
    `role_id`          VARCHAR(16)  NOT NULL                  COMMENT '角色 ID',
    `scope`            VARCHAR(45)  NOT NULL                  COMMENT '适用领域',
    `permission`       VARCHAR(45)  NOT NULL                  COMMENT '访问许可',
    `revision`         DOUBLE       NOT NULL                  COMMENT '修订版本号',
    `created_at`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`       VARCHAR(16)  NOT NULL                  COMMENT '创建者用户 ID',
    `last_modified_at` DATETIME     DEFAULT NULL              COMMENT '最后更新时间',
    `last_modified_by` VARCHAR(16)  DEFAULT NULL              COMMENT '最后更新者用户 ID',
    `deleted`          BIT(1)       DEFAULT 0                 COMMENT '是否已删除',
    `deleted_at`       DATETIME     DEFAULT NULL              COMMENT '删除时间',
    `deleted_by`       VARCHAR(16)  DEFAULT NULL              COMMENT '删除操作者用户 ID',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '组织角色权限';

DROP TABLE IF EXISTS `role_member`;
CREATE TABLE `role_member` (
    `id`               VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `company_id`       VARCHAR(16)  NOT NULL                  COMMENT '公司 ID',
    `org_id`           VARCHAR(16)  NOT NULL                  COMMENT '组织 ID',
    `role_id`          VARCHAR(16)  NOT NULL                  COMMENT '角色 ID',
    `employee_id`      VARCHAR(16)  NOT NULL                  COMMENT '成员职员 ID',
    `user_id`          VARCHAR(16)  NOT NULL                  COMMENT '成员用户 ID',
    `revision`         DOUBLE       NOT NULL                  COMMENT '修订版本号',
    `created_at`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`       VARCHAR(16)  NOT NULL                  COMMENT '创建者用户 ID',
    `last_modified_at` DATETIME     DEFAULT NULL              COMMENT '最后更新时间',
    `last_modified_by` VARCHAR(16)  DEFAULT NULL              COMMENT '最后更新者用户 ID',
    `deleted`          BIT(1)       DEFAULT 0                 COMMENT '是否已删除',
    `deleted_at`       DATETIME     DEFAULT NULL              COMMENT '删除时间',
    `deleted_by`       VARCHAR(16)  DEFAULT NULL              COMMENT '删除操作者用户 ID',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '组织角色成员';
