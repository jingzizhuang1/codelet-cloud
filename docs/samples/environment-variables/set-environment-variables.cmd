#!/usr/bin/env powershell -File

SETX CC_CONFIG_SERVER             "192.168.1.247:8001"                          # 配置服务器地址
SETX CC_CONFIG_NAME               "codelet"                                     # 配置文件名
SETX CC_CONFIG_GIT_URI            "codelet@192.168.1.247:cloud/config-profiles" # 配置文件 Git 库地址
SETX CC_CONSUL_SERVER_HOST        "192.168.1.247"                               # Consul 注册中心服务器地址
SETX CC_CONSUL_SERVER_PORT        "8940"                                        # Consul 注册中心服务器端口
SETX CC_KAFKA_BROKERS             "192.168.1.247:8960"                          # Kafka Broker 列表
SETX CC_REDIS_SERVER_HOST         "192.168.1.247"                               # Redis 服务器地址
SETX CC_REDIS_SERVER_PORT         "6301"                                        # Redis 服务器端口
SETX CC_CAPTCHA_FONT_DIR          "C:/var/codelet/resources/fonts/captcha/"     # 图形验证码字体文件路径
SETX CC_MYSQL_SERVER              "192.168.1.247:3306"                          # 默认数据库

# 在新的 PowerShell 窗口中执行以下命令
SETX CC_MYSQL_SERVER_PARAMETER    "$Env:CC_MYSQL_SERVER"                        # 业务参数数据库
SETX CC_MYSQL_SERVER_AUTH         "$Env:CC_MYSQL_SERVER"                        # 认证数据库
SETX CC_MYSQL_SERVER_VERIFICATION "$Env:CC_MYSQL_SERVER"                        # 验证码数据库
SETX CC_MYSQL_SERVER_NOTIFICATION "$Env:CC_MYSQL_SERVER"                        # 通知/消息数据库
SETX CC_MYSQL_SERVER_USER         "$Env:CC_MYSQL_SERVER"                        # 用户账号数据库
SETX CC_MYSQL_SERVER_ORGANIZATION "$Env:CC_MYSQL_SERVER"                        # 组织数据库
SETX CC_MYSQL_SERVER_EMPLOYEE     "$Env:CC_MYSQL_SERVER"                        # 职员数据库
SETX CC_MYSQL_SERVER_ROLE         "$Env:CC_MYSQL_SERVER"                        # 角色/权限数据库
SETX CC_MYSQL_SERVER_TAG          "$Env:CC_MYSQL_SERVER"                        # 标签数据库
SETX CC_MYSQL_SERVER_FILE         "$Env:CC_MYSQL_SERVER"                        # 文件数据库
SETX CC_MYSQL_SERVER_COMMENT      "$Env:CC_MYSQL_SERVER"                        # 批注/评论数据库
SETX CC_MYSQL_SERVER_COMMUNITY    "$Env:CC_MYSQL_SERVER"                        # 社区/问答数据库
SETX CC_MYSQL_SERVER_POST         "$Env:CC_MYSQL_SERVER"                        # 帖子数据库
SETX CC_MYSQL_SERVER_TASK         "$Env:CC_MYSQL_SERVER"                        # 任务数据库
SETX CC_MYSQL_SERVER_PRODUCT      "$Env:CC_MYSQL_SERVER"                        # 产品数据库
SETX CC_MYSQL_SERVER_SERVICE      "$Env:CC_MYSQL_SERVER"                        # 平台服务数据库
SETX CC_MYSQL_SERVER_ORDER        "$Env:CC_MYSQL_SERVER"                        # 订单数据库
SETX CC_MYSQL_SERVER_STATISTICS   "$Env:CC_MYSQL_SERVER"                        # 统计数据库
SETX CC_MYSQL_SERVER_MONOLITHIC   "$Env:CC_MYSQL_SERVER"                        # 单实例启动模式数据库
