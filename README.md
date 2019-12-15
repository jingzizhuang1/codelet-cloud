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


## 报表模块所需组件说明

运行 JasperReport 所需的以下组件无法从 Maven 公共仓库取得，需要手动安装到本地 Maven 库，或上传到 Nexus Repository Manager：

* `itext-2.1.7.js7.jar`（用于实现 PDF 生成）
* `jasperreports-fonts-6.1.1.jar`（为对 `jasperreports-fonts` 的重新打包，添加了所需的中文字体）

在组件的 JAR 文件所在的路径（`report/assets`）下执行以下命令以将其安装到本地 Maven 库：

```bash
$ mvn install:install-file -Dfile=./itext-2.1.7.js7.jar -DgroupId=com.lowagie -DartifactId=itext -Dversion=2.1.7.js7 -Dpackaging=jar
$ mvn install:install-file -Dfile=./jasperreports-fonts-6.1.1.jar -DgroupId=net.sf.jasperreports -DartifactId=jasperreports-fonts -Dversion=6.1.1 -Dpackaging=jar
```
