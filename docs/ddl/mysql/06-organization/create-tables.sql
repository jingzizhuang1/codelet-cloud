-- -----------------------------------------------------------------------------
-- Codelet Cloud 数据库表创建脚本
-- 数据库：MySQL Server 5.7
-- 模　块：组织
-- -----------------------------------------------------------------------------
USE `codelet_cloud_organization`;

DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization` (
    `id`               VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `type`             VARCHAR(45)  NOT NULL                  COMMENT '组织类型',
    `company_id`       VARCHAR(16)  DEFAULT NULL              COMMENT '公司 ID',
    `division_type`    VARCHAR(45)  DEFAULT NULL              COMMENT '事业部类型',
    `name`             VARCHAR(45)  NOT NULL                  COMMENT '名称',
    `name_pinyin`      VARCHAR(45)  NOT NULL                  COMMENT '名称拼音',
    `description`      VARCHAR(255) DEFAULT NULL              COMMENT '描述',
    `revision`         DOUBLE       NOT NULL                  COMMENT '修订版本号',
    `created_at`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`       VARCHAR(16)  NOT NULL                  COMMENT '创建者用户 ID',
    `last_modified_at` DATETIME     DEFAULT NULL              COMMENT '最后更新时间',
    `last_modified_by` VARCHAR(16)  DEFAULT NULL              COMMENT '最后更新者用户 ID',
    `disabled`         BIT(1)       DEFAULT 0                 COMMENT '是否已停用',
    `disabled_at`      DATETIME     DEFAULT NULL              COMMENT '停用时间',
    `disabled_by`      VARCHAR(16)  DEFAULT NULL              COMMENT '停用操作者用户 ID',
    `deleted`          BIT(1)       DEFAULT 0                 COMMENT '是否已删除',
    `deleted_at`       DATETIME     DEFAULT NULL              COMMENT '删除时间',
    `deleted_by`       VARCHAR(16)  DEFAULT NULL              COMMENT '删除操作者用户 ID',
    PRIMARY KEY (`id`),
    INDEX `company_id_deleted` (`company_id`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '组织';

DROP TABLE IF EXISTS `organization_hierarchy`;
CREATE TABLE `organization_hierarchy` (
    `id`               VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `org_id`           VARCHAR(16)  NOT NULL                  COMMENT '对象 ID',
    `company_id`       VARCHAR(16)  NOT NULL                  COMMENT '公司 ID',
    `division_id`      VARCHAR(16)  DEFAULT NULL              COMMENT '事业部 ID',
    `division_type`    VARCHAR(45)  DEFAULT NULL              COMMENT '事业部类型',
    `parent_id`        VARCHAR(16)  NOT NULL                  COMMENT '上级 ID',
    `depth`            INTEGER(2)   DEFAULT 0                 COMMENT '最大层级深度',
    `sort_order`       INTEGER(11)  DEFAULT 0                 COMMENT '排序顺序',
    `revision`         DOUBLE       NOT NULL                  COMMENT '修订版本号',
    `created_at`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`       VARCHAR(16)  NOT NULL                  COMMENT '创建者用户 ID',
    `last_modified_at` DATETIME     DEFAULT NULL              COMMENT '最后更新时间',
    `last_modified_by` VARCHAR(16)  DEFAULT NULL              COMMENT '最后更新者用户 ID',
    `deleted`          BIT(1)       DEFAULT 0                 COMMENT '是否已删除',
    `deleted_at`       DATETIME     DEFAULT NULL              COMMENT '删除时间',
    `deleted_by`       VARCHAR(16)  DEFAULT NULL              COMMENT '删除操作者用户 ID',
    PRIMARY KEY (`id`),
    INDEX `org_id_parent_id_deleted` (`org_id`, `parent_id`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '组织层级结构';

-- 无上级组织的组织视图
CREATE OR REPLACE VIEW `organization_with_no_parent` AS (
    SELECT
        `o`.`id`,
        `o`.`company_id`
    FROM
        `organization` AS `o`
        LEFT OUTER JOIN `organization_hierarchy` AS `h`
            ON  `h`.`org_id`  = `o`.`id`
            AND `h`.`deleted` = 0
    WHERE
        `o`.`deleted`    = 0
    AND `h`.`parent_id`  IS NULL
);
