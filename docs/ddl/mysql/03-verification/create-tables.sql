-- -----------------------------------------------------------------------------
-- Codelet Cloud 数据库表创建脚本
-- 数据库：MySQL Server 5.7
-- 模　块：人机验证/验证码
-- -----------------------------------------------------------------------------
USE `codelet_cloud_verification`;

DROP TABLE IF EXISTS `verification`;
CREATE TABLE `verification` (
    `id`                 VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `key_type`           VARCHAR(16)  NOT NULL                  COMMENT '验证码类型（电子邮箱地址、手机号码）',
    `verify_key`         VARCHAR(255) NOT NULL                  COMMENT 'KEY（如电子邮箱地址、手机号码）',
    `code`               VARCHAR(255) NOT NULL                  COMMENT '验证码',
    `purpose`            VARCHAR(45)  NOT NULL                  COMMENT '验证目的（用户注册、用户登录、密码重置、电子邮箱或手机号码绑定）',
    `verified_times`     INTEGER(2)   DEFAULT 0                 COMMENT '已验证次数',
    `created_at`         DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`         VARCHAR(16)  DEFAULT NULL              COMMENT '创建者用户 ID',
    `remote_addr`        VARCHAR(39)  NOT NULL                  COMMENT '客户端远程 IP 地址',
    `user_agent`         VARCHAR(255) NOT NULL                  COMMENT '用户代理字符串',
    `expires_at`         DATETIME     NOT NULL                  COMMENT '过期时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '验证码';

DROP TABLE IF EXISTS `verification_timer`;
CREATE TABLE `verification_timer` (
    `id`                 VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `key_type`           VARCHAR(16)  NOT NULL                  COMMENT '验证码类型（电子邮箱地址、手机号码）',
    `verify_key`         VARCHAR(255) NOT NULL                  COMMENT 'KEY（如电子邮箱地址、手机号码）',
    `purpose`            VARCHAR(45)  NOT NULL                  COMMENT '验证目的（用户注册、用户登录、密码重置、电子邮箱或手机号码绑定）',
    `created_at`         DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（周期起始时间）',
    `last_sent_at`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '最后发送时间',
    `times`              INTEGER(11)  NOT NULL DEFAULT 0        COMMENT '已发送次数',
    PRIMARY KEY (`id`),
    UNIQUE KEY `vt_kt_vk_pp` (`key_type`, `verify_key`, `purpose`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '验证码计时器';

DROP TABLE IF EXISTS `verification_configuration`;
CREATE TABLE `verification_configuration` (
    `id`                 VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `key_type`           VARCHAR(16)  NOT NULL                  COMMENT '验证码类型（电子邮箱地址、手机号码）',
    `purpose`            VARCHAR(45)  NOT NULL                  COMMENT '验证目的（用户注册、用户登录、密码重置、电子邮箱或手机号码绑定）',
    `code_length`        INTEGER(2)   NOT NULL DEFAULT 6        COMMENT '验证码长度',
    `code_charset`       VARCHAR(45)  NOT NULL DEFAULT 'NUMBER' COMMENT '验证码字符集',
    `ttl`                INTEGER(11)  NOT NULL DEFAULT 300      COMMENT '有效时长（秒）',
    `interval_seconds`   INTEGER(11)  NOT NULL DEFAULT 60       COMMENT '发送间隔最小时长（秒）',
    `rate_limit_period`  INTEGER(11)  NOT NULL DEFAULT 3600     COMMENT '发送频率限制周期（秒）',
    `rate_limit_times`   INTEGER(11)  NOT NULL DEFAULT 3        COMMENT '发送频率限制次数',
    `max_verify_times`   INTEGER(2)   DEFAULT 2                 COMMENT '最大验证次数',
    `template_id`        VARCHAR(16)  NOT NULL                  COMMENT '通知消息模版 ID',
    `revision`           DOUBLE       NOT NULL                  COMMENT '修订版本号',
    `created_at`         DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`         VARCHAR(16)  DEFAULT NULL              COMMENT '创建者用户 ID',
    `last_modified_at`   DATETIME     DEFAULT NULL              COMMENT '最后更新时间',
    `last_modified_by`   VARCHAR(16)  DEFAULT NULL              COMMENT '最后更新者用户 ID',
    `disabled`           BIT(1)       DEFAULT 0                 COMMENT '是否已停用',
    `disabled_at`        DATETIME     DEFAULT NULL              COMMENT '停用时间',
    `disabled_by`        VARCHAR(16)  DEFAULT NULL              COMMENT '停用操作者用户 ID',
    `deleted`            BIT(1)       DEFAULT 0                 COMMENT '是否已删除',
    `deleted_at`         DATETIME     DEFAULT NULL              COMMENT '删除时间',
    `deleted_by`         VARCHAR(16)  DEFAULT NULL              COMMENT '删除操作者用户 ID',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '验证码配置';

-- 验证 KEY 的验证码合计
CREATE OR REPLACE VIEW `verification_sum` AS (
    SELECT
        MAX(`id`) AS `id`,
        `key_type`,
        `verify_key`,
        `purpose`,
        GROUP_CONCAT(`code` SEPARATOR ',') AS `codes`,
        SUM(`verified_times`) AS `verified_times`
    FROM
        `verification`
    WHERE
        `expires_at` > CURRENT_TIMESTAMP
    GROUP BY
        `key_type`,
        `verify_key`,
        `purpose`
);
