-- -----------------------------------------------------------------------------
-- 数据库初始化设置。
-- -----------------------------------------------------------------------------

-- 设置时区为东八区，配置文件属性：default-time_zone=+8:00
SET GLOBAL `time_zone` = '+8:00';

-- 设置 GROUP_CONCAT 的最大字符串长度，配置文件属性：group_concat_max_len=4096
SET GLOBAL `group_concat_max_len` = 4096;

-- 设置最大递归次数，配置文件属性：max_sp_recursion_depth=10
SET GLOBAL `max_sp_recursion_depth` = 10;

-- 创建用户
SET GLOBAL `validate_password_policy` = 0;
CREATE USER 'codelet'@'%' IDENTIFIED BY '1qazxsw2';

-- 创建数据库
CREATE SCHEMA `codelet_cloud`              DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_parameter`    DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_auth`         DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_verification` DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_notification` DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_user`         DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_organization` DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_employee`     DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_role`         DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_tag`          DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_file`         DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_comment`      DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_community`    DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_post`         DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_task`         DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_product`      DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_service`      DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_order`        DEFAULT CHARACTER SET utf8;
CREATE SCHEMA `codelet_cloud_statistics`   DEFAULT CHARACTER SET utf8;

-- 将数据库访问权限授予用户
GRANT SELECT         ON `performance_schema`.*         TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2';
GRANT ALL PRIVILEGES ON `codelet_cloud`.*              TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_parameter`.*    TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_auth`.*         TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_verification`.* TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_notification`.* TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_user`.*         TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_organization`.* TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_employee`.*     TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_role`.*         TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_tag`.*          TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_file`.*         TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_comment`.*      TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_community`.*    TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_post`.*         TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_task`.*         TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_product`.*      TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_service`.*      TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_order`.*        TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON `codelet_cloud_statistics`.*   TO 'codelet'@'%' IDENTIFIED BY '1qazxsw2' WITH GRANT OPTION;
FLUSH PRIVILEGES;
