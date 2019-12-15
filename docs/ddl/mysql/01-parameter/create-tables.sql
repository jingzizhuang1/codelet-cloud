-- -----------------------------------------------------------------------------
-- Codelet Cloud 数据库表创建脚本
-- 数据库：MySQL Server 5.7
-- 模　块：业务参数
-- -----------------------------------------------------------------------------
USE `codelet_cloud_parameter`;

DROP TABLE IF EXISTS `parameter`;
CREATE TABLE `parameter` (
    `id`                 VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `app_id`             VARCHAR(45)  NOT NULL DEFAULT ''       COMMENT '应用 ID',
    `name`               VARCHAR(45)  NOT NULL                  COMMENT '参数名',
    `value`              VARCHAR(255) NOT NULL                  COMMENT '参数值',
    `description`        VARCHAR(255) NOT NULL                  COMMENT '参数说明',
    `revision`           DOUBLE       NOT NULL                  COMMENT '修订版本号',
    `created_at`         DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`         VARCHAR(16)  NOT NULL                  COMMENT '创建者用户 ID',
    `last_modified_at`   DATETIME     DEFAULT NULL              COMMENT '最后更新时间',
    `last_modified_by`   VARCHAR(16)  DEFAULT NULL              COMMENT '最后更新者用户 ID',
    `deleted`            BIT(1)       DEFAULT 0                 COMMENT '是否已删除',
    `deleted_at`         DATETIME     DEFAULT NULL              COMMENT '删除时间',
    `deleted_by`         VARCHAR(16)  DEFAULT NULL              COMMENT '删除操作者用户 ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `pm_appid_name` (`app_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '业务参数';
