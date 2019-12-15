-- -----------------------------------------------------------------------------
-- Codelet Cloud 数据库表创建脚本
-- 数据库：MySQL Server 5.7
-- 模　块：用户
-- -----------------------------------------------------------------------------
USE `codelet_cloud_user`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`               VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `type`             VARCHAR(15)  NOT NULL                  COMMENT '账号类型（如系统管理员、用户、设备等）',
    `name`             VARCHAR(45)  NOT NULL                  COMMENT '姓名',
    `name_pinyin`      VARCHAR(45)  NOT NULL                  COMMENT '姓名拼音',
    `mobile`           VARCHAR(15)  DEFAULT NULL              COMMENT '手机号码',
    `email`            VARCHAR(45)  DEFAULT NULL              COMMENT '电子邮箱地址',
    `birth_date`       DATETIME     DEFAULT NULL              COMMENT '出生日期',
    `gender`           VARCHAR(45)  DEFAULT NULL              COMMENT '性别',
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
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '用户账号';
