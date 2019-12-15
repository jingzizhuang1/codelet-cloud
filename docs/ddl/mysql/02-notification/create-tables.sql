-- -----------------------------------------------------------------------------
-- Codelet Cloud 数据库表创建脚本
-- 数据库：MySQL Server 5.7
-- 模　块：通知
-- -----------------------------------------------------------------------------
USE `codelet_cloud_notification`;

DROP TABLE IF EXISTS `mail_configuration`;
CREATE TABLE `mail_configuration` (
    `id`                 VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `name`               VARCHAR(255) NOT NULL                  COMMENT '配置名称',
    `host`               VARCHAR(255) NOT NULL                  COMMENT '主机名',
    `port`               INTEGER(5)   NOT NULL                  COMMENT '端口号',
    `protocol`           VARCHAR(45)  NOT NULL DEFAULT 'smtp'   COMMENT '协议',
    `sender_name`        VARCHAR(45)  NOT NULL                  COMMENT '发送者名称',
    `username`           VARCHAR(255) NOT NULL                  COMMENT '账号',
    `password`           VARCHAR(255) NOT NULL                  COMMENT '密码',
    `start_tls_enabled`  BIT(1)       NOT NULL DEFAULT 1        COMMENT '是否启用 StartTLS',
    `start_tls_required` BIT(1)       NOT NULL DEFAULT 1        COMMENT '是否必须使用 StartTLS',
    `connection_timeout` INTEGER(11)  NOT NULL DEFAULT 60000    COMMENT '连接超时时长（mail.smtp.connetiontimeout）',
    `read_timeout`       INTEGER(11)  NOT NULL DEFAULT 60000    COMMENT '读取超时时长（mail.smtp.timeout）',
    `write_timeout`      INTEGER(11)  NOT NULL DEFAULT 60000    COMMENT '写入超时时长（mail.smtp.writetimeout）',
    `is_default`         BIT(1)       NOT NULL DEFAULT 0        COMMENT '是否为默认配置',
    `revision`           DOUBLE       NOT NULL                  COMMENT '修订版本号',
    `created_at`         DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`         VARCHAR(16)  NOT NULL                  COMMENT '创建者用户 ID',
    `last_modified_at`   DATETIME     DEFAULT NULL              COMMENT '最后更新时间',
    `last_modified_by`   VARCHAR(16)  DEFAULT NULL              COMMENT '最后更新者用户 ID',
    `disabled`           BIT(1)       DEFAULT 0                 COMMENT '是否已停用',
    `disabled_at`        DATETIME     DEFAULT NULL              COMMENT '停用时间',
    `disabled_by`        VARCHAR(16)  DEFAULT NULL              COMMENT '停用操作者用户 ID',
    `deleted`            BIT(1)       DEFAULT 0                 COMMENT '是否已删除',
    `deleted_at`         DATETIME     DEFAULT NULL              COMMENT '删除时间',
    `deleted_by`         VARCHAR(16)  DEFAULT NULL              COMMENT '删除操作者用户 ID',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '电子邮件发送配置';

DROP TABLE IF EXISTS `sms_configuration`;
CREATE TABLE `sms_configuration` (
    `id`                 VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `name`               VARCHAR(255) NOT NULL                  COMMENT '配置名称',
    `provider`           VARCHAR(45)  NOT NULL                  COMMENT '短信服务提供商',
    `username`           VARCHAR(255) NOT NULL                  COMMENT '账号名或访问 KEY',
    `password`           VARCHAR(255) NOT NULL                  COMMENT '账号密码或访问密钥',
    `sign_name`          VARCHAR(45)  DEFAULT NULL              COMMENT '短信签名',
    `is_default`         BIT(1)       NOT NULL DEFAULT 0        COMMENT '是否为默认配置',
    `revision`           DOUBLE       NOT NULL                  COMMENT '修订版本号',
    `created_at`         DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`         VARCHAR(16)  NOT NULL                  COMMENT '创建者用户 ID',
    `last_modified_at`   DATETIME     DEFAULT NULL              COMMENT '最后更新时间',
    `last_modified_by`   VARCHAR(16)  DEFAULT NULL              COMMENT '最后更新者用户 ID',
    `disabled`           BIT(1)       DEFAULT 0                 COMMENT '是否已停用',
    `disabled_at`        DATETIME     DEFAULT NULL              COMMENT '停用时间',
    `disabled_by`        VARCHAR(16)  DEFAULT NULL              COMMENT '停用操作者用户 ID',
    `deleted`            BIT(1)       DEFAULT 0                 COMMENT '是否已删除',
    `deleted_at`         DATETIME     DEFAULT NULL              COMMENT '删除时间',
    `deleted_by`         VARCHAR(16)  DEFAULT NULL              COMMENT '删除操作者用户 ID',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '短信发送配置';

DROP TABLE IF EXISTS `notification_template`;
CREATE TABLE `notification_template` (
    `id`                 VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `name`               VARCHAR(255) NOT NULL                  COMMENT '名称',
    `description`        TEXT         DEFAULT NULL              COMMENT '描述',
    `configuration_id`   VARCHAR(16)  DEFAULT NULL              COMMENT '电子邮件或短信发送配置 ID',
    `tags`               TEXT         DEFAULT NULL              COMMENT '标签',
    `revision`           DOUBLE       NOT NULL                  COMMENT '修订版本号',
    `created_at`         DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`         VARCHAR(16)  NOT NULL                  COMMENT '创建者用户 ID',
    `last_modified_at`   DATETIME     DEFAULT NULL              COMMENT '最后更新时间',
    `last_modified_by`   VARCHAR(16)  DEFAULT NULL              COMMENT '最后更新者用户 ID',
    `disabled`           BIT(1)       DEFAULT 0                 COMMENT '是否已停用',
    `disabled_at`        DATETIME     DEFAULT NULL              COMMENT '停用时间',
    `disabled_by`        VARCHAR(16)  DEFAULT NULL              COMMENT '停用操作者用户 ID',
    `deleted`            BIT(1)       DEFAULT 0                 COMMENT '是否已删除',
    `deleted_at`         DATETIME     DEFAULT NULL              COMMENT '删除时间',
    `deleted_by`         VARCHAR(16)  DEFAULT NULL              COMMENT '删除操作者用户 ID',
    PRIMARY KEY (`id`),
    INDEX `availability` (`disabled`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '通知消息模版';

DROP TABLE IF EXISTS `notification_template_content`;
CREATE TABLE `notification_template_content` (
    `id`                 VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `template_id`        VARCHAR(16)  NOT NULL                  COMMENT '模版 ID',
    `language_code`      VARCHAR(5)   NOT NULL                  COMMENT '语言代码',
    `subject`            TEXT         DEFAULT NULL              COMMENT '标题模版',
    `content_type`       VARCHAR(255) DEFAULT 'text/plain'      COMMENT '内容类型',
    `content`            TEXT         NOT NULL                  COMMENT '内容模版',
    `revision`           DOUBLE       NOT NULL                  COMMENT '修订版本号',
    `created_at`         DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`         VARCHAR(16)  NOT NULL                  COMMENT '创建者用户 ID',
    `last_modified_at`   DATETIME     DEFAULT NULL              COMMENT '最后更新时间',
    `last_modified_by`   VARCHAR(16)  DEFAULT NULL              COMMENT '最后更新者用户 ID',
    `disabled`           BIT(1)       DEFAULT 0                 COMMENT '是否已停用',
    `disabled_at`        DATETIME     DEFAULT NULL              COMMENT '停用时间',
    `disabled_by`        VARCHAR(16)  DEFAULT NULL              COMMENT '停用操作者用户 ID',
    `deleted`            BIT(1)       DEFAULT 0                 COMMENT '是否已删除',
    `deleted_at`         DATETIME     DEFAULT NULL              COMMENT '删除时间',
    `deleted_by`         VARCHAR(16)  DEFAULT NULL              COMMENT '删除操作者用户 ID',
    PRIMARY KEY (`id`),
    INDEX `availability` (`template_id`, `disabled`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '通知消息模版本地化内容';

DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
    `id`                 VARCHAR(16)  NOT NULL                  COMMENT 'ID',
    `biz_type`           VARCHAR(45)  NOT NULL                  COMMENT '业务类型',
    `sender_type`        VARCHAR(45)  NOT NULL                  COMMENT '发送者类型',
    `sender_id`          VARCHAR(16)  NOT NULL                  COMMENT '发送者 ID',
    `receiver_id`        VARCHAR(16)  NOT NULL                  COMMENT '通知对象用户 ID',
    `title`              VARCHAR(255) NOT NULL                  COMMENT '标题',
    `content_type`       VARCHAR(255) DEFAULT 'text/plain'      COMMENT '内容类型',
    `content`            TEXT         NOT NULL                  COMMENT '内容',
    `target_type`        VARCHAR(45)  DEFAULT NULL              COMMENT '目标对象类型',
    `target_id`          VARCHAR(16)  DEFAULT NULL              COMMENT '目标对象 ID',
    `created_at`         DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`         VARCHAR(16)  DEFAULT NULL              COMMENT '创建者用户 ID',
    `read_at`            DATETIME     DEFAULT NULL              COMMENT '阅读时间',
    `expires_at`         DATETIME     DEFAULT NULL              COMMENT '过期时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '通知';

-- 模版可用语言视图
CREATE OR REPLACE VIEW `notification_template_language_codes` AS (
    SELECT
        `t`.`id`,
        GROUP_CONCAT(`c`.`language_code` SEPARATOR ',') AS `language_codes`
    FROM
        `notification_template` AS `t`
        STRAIGHT_JOIN `notification_template_content` AS `c`
            ON  `c`.`template_id` = `t`.`id`
            AND `c`.`disabled`    = 0
            AND `c`.`deleted`     = 0
    WHERE
        `t`.`deleted`  = 0
    AND `t`.`disabled` = 0
    GROUP BY
        `t`.`id`
);

-- 可用本地化消息模版视图
CREATE OR REPLACE VIEW `notification_template_content_available` AS (
    SELECT
        `t`.`configuration_id`,
        `c`.*
    FROM
        `notification_template` AS `t`
        STRAIGHT_JOIN `notification_template_content` AS `c`
            ON  `c`.`template_id` = `t`.`id`
            AND `c`.`disabled`    = 0
            AND `c`.`deleted`     = 0
    WHERE
        `t`.`deleted`  = 0
    AND `t`.`disabled` = 0
);
