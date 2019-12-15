# 依赖组件安装说明

## 组件说明

运行 JasperReport 所需的以下组件无法从 Maven 公共仓库取得，需要手动安装到本地 Maven 库，或上传到 Nexus Repository Manager：

* `itext-2.1.7.js7.jar`（用于实现 PDF 生成）
* `jasperreports-fonts-6.1.1.jar`（为对 `jasperreports-fonts` 的重新打包，添加了所需的中文字体）

> 待调查问题：本地执行 `mvn clean install` 时可以从 Maven 公共仓库获取 `itext-2.1.7.js7.jar`。在 `192.168.1.247`上执行 `mvn clean install` 时无法获取，必须通过下文的方式上传到 Nexus Repository Manager。为什么？

> 待确认问题： 
> * 中文字体问题是否可以通过其他方式解决？
> * 是否应避免使用微软雅黑字体以规避可能的版权纠纷？

## 手动安装到本地 Maven 库

在组件的 JAR 文件所在的路径下执行以下命令以将其安装到本地 Maven 库：

```bash
$ mvn install:install-file -Dfile=./itext-2.1.7.js7.jar -DgroupId=com.lowagie -DartifactId=itext -Dversion=2.1.7.js7 -Dpackaging=jar
$ mvn install:install-file -Dfile=./jasperreports-fonts-msyh-6.1.1.jar -DgroupId=net.sf.jasperreports -DartifactId=jasperreports-fonts -Dversion=6.1.1 -Dpackaging=jar
```

## 上传到 Nexus Repository Manager

登录到 Nexus Repository Manager，选择 `Upload > codelet-cloud-releases`。

分别选择组件的 JAR 文件，然后填写以下内容并上传：

|组件|Extension|Group ID|Artifact ID|Version|Packaging|
|:---:|:---:|:---:|:---:|:---:|:---:|
|`itext-2.1.7.js7.jar`|`jar`|`com.lowagie`|`itext`|`2.1.7.js7`|`jar`|
|`jasperreports-fonts-6.1.1.jar`|`jar`|`net.sf.jasperreports`|`jasperreports-fonts`|`6.1.1`|`jar`|
