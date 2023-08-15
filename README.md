# haha-search-back

哈哈搜索后端(基于 spring 全家桶、mybatis-plus、elastic-search)

## idea 启动项目

所需的 elasticsearch 和 redis 在此链接：https://pan.baidu.com/s/1f6zBb1qV3MjG7nrWEVYwgw?pwd=r3me

1. 加载 Maven 文件，在 pom.xml 点击右上方图标自动装载（**必须**）
2. 修改 src/main/resources/application.yml 中的 mysql 数据库信息，启动 mysql（**必须**）
3. 解压 elasticsearch 7.17.3，点击 elasticsearch 7.17.3/bin/elasticsearch.bat 启动搜索引擎（**必须**）
4. 安装 redis 5.0.14，点击 redis/redis-server.exe 启动（**必须**）
5. 若需爬虫功能，点击 spider/FlaskApp.py 启动爬虫（**非必须**）
6. 选择 edu/zuel/hahasearch/HahaSearchApplication.java 程序并启动（**必须**）

## CMD 启动项目

完成 idea 前 5 项前置后，执行以下命令

```bash
mvn package -DskipTests
```

```bash
nohup java -jar haha-search-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod &
```
