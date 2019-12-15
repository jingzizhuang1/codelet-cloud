# Codelet Cloud

## 服务器环境搭建

要运行 Codelet Cloud 需要：

- 使用 Consul 作为服务注册中心
- 使用 Git 管理配置文件
- 使用 MySQL 作为持久化数据库
- 使用 Redis 作为缓存
- 使用 Kafka 作为事件总线/消息队列

请参考 [docs.codelet.net](https://docs.codelet.net) 的上的以下说明：

- [服务器环境搭建](https://docs.codelet.net/docs/framework/continuous-integration/install-components/)
- [配置并启动 Consul](https://docs.codelet.net/docs/framework/continuous-integration/configure-consul/)

Consul 的配置及相关脚本可参考 `docs/samples/consul` 下的内容。

## 开发环境搭建

Mac OS X 下请参考 [Mac OS X 开发环境搭建说明](https://docs.codelet.net/docs/framework/developer-manual-for-backend/mac-os-x/)。

Windows 下请参考 [Windows 开发环境搭建说明](https://docs.codelet.net/docs/framework/developer-manual-for-backend/windows/)。

> 注意：上述文档的 Maven 配置针对使用内网私有库的情况，请忽略。

## 环境变量设置

Mac OS X 下请参考 `docs/samples/environment-variables/bashrc` 中的内容。

Windows 下请参考 `docs/samples/environment-variables/set-environment-variables.cmd` 中的内容。
