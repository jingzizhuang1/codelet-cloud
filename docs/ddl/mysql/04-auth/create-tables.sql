-- -----------------------------------------------------------------------------
-- Codelet Cloud 数据库表创建脚本
-- 数据库：MySQL Server 5.7
-- 模　块：认证/授权
-- -----------------------------------------------------------------------------
USE `codelet_cloud_auth`;

DROP TABLE IF EXISTS `access_token`;
CREATE TABLE `access_token` (
    `id`               VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `user_id`          VARCHAR(16)  NOT NULL                  COMMENT '所有者用户 ID',
    `app_id`           VARCHAR(16)  DEFAULT NULL              COMMENT '应用 ID',
    `scope`            VARCHAR(255) NOT NULL                  COMMENT '作用域',
    `remote_addr`      VARCHAR(39)  NOT NULL                  COMMENT '客户端 IP 地址',
    `client`           VARCHAR(255) DEFAULT NULL              COMMENT '客户端信息',
    `user_agent`       VARCHAR(255) DEFAULT NULL              COMMENT '用户代理字符串',
    `user_revision`    DOUBLE       DEFAULT NULL              COMMENT '用户信息更新版本号',
    `created_at`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`       VARCHAR(16)  NOT NULL                  COMMENT '创建者用户 ID',
    `refreshed_at`     DATETIME     DEFAULT NULL              COMMENT '刷新时间',
    `refreshed_by`     VARCHAR(16)  DEFAULT NULL              COMMENT '刷新者用户 ID',
    `expires_at`       DOUBLE       DEFAULT NULL              COMMENT '过期时间',
    `disabled`         BIT(1)       DEFAULT 0                 COMMENT '是否已停用',
    `disabled_at`      DATETIME     DEFAULT NULL              COMMENT '停用时间',
    `disabled_by`      VARCHAR(16)  DEFAULT NULL              COMMENT '停用操作者用户 ID',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '访问令牌';

DROP TABLE IF EXISTS `credential`;
CREATE TABLE `credential` (
    `id`               VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `user_id`          VARCHAR(16)  NOT NULL                  COMMENT '所有者用户 ID',
    `type`             VARCHAR(16)  NOT NULL                  COMMENT '类型（如账号名、电子邮箱地址、手机号码等）',
    `credential`       VARCHAR(45)  NOT NULL                  COMMENT 'KEY',
    `revision`         DOUBLE       NOT NULL                  COMMENT '修订版本号',
    `created_at`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`       VARCHAR(16)  NOT NULL                  COMMENT '创建者用户 ID',
    `last_modified_at` DATETIME     DEFAULT NULL              COMMENT '最后更新时间',
    `last_modified_by` VARCHAR(16)  DEFAULT NULL              COMMENT '最后更新者用户 ID',
    `deleted`          BIT(1)       DEFAULT 0                 COMMENT '是否已删除',
    `deleted_at`       DATETIME     DEFAULT NULL              COMMENT '删除时间',
    `deleted_by`       VARCHAR(16)  DEFAULT NULL              COMMENT '删除操作者用户 ID',
    PRIMARY KEY (`id`),
    INDEX `user_credential` (`deleted`, `credential`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '认证凭证';

DROP TABLE IF EXISTS `user_password`;
CREATE TABLE `user_password` (
    `id`               VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `user_id`          VARCHAR(16)  NOT NULL                  COMMENT '所有者用户 ID',
    `password`         VARCHAR(128) NOT NULL                  COMMENT '登录密码',
    `strength`         INTEGER(1)   NOT NULL                  COMMENT '密码强度',
    `hash`             VARCHAR(32)  NOT NULL                  COMMENT '散列摘要（用于查找使用过的密码）',
    `created_at`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`       VARCHAR(16)  NOT NULL                  COMMENT '创建者用户 ID',
    `deleted`          BIT(1)       DEFAULT 0                 COMMENT '是否已删除',
    `deleted_at`       DATETIME     DEFAULT NULL              COMMENT '删除时间',
    `deleted_by`       VARCHAR(16)  DEFAULT NULL              COMMENT '删除操作者用户 ID',
    PRIMARY KEY (`id`),
    INDEX `user_password` (`deleted`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '用户登录密码';

-- 登录凭证
CREATE OR REPLACE VIEW `credential_password` AS
    SELECT
        `c`.`id`,
        `c`.`user_id`,
        `c`.`credential`,
        `p`.`password`
    FROM
        `credential` AS `c`
        STRAIGHT_JOIN `user_password` AS `p`
            ON `p`.`user_id` = `c`.`user_id`
            AND `p`.`deleted` = 0
    WHERE
        `c`.`deleted` = 0
;
