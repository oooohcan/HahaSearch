# haha-search-back
哈哈搜索后端(基于spring全家桶、mybatis-plus、elastic-search)

## idea启动项目
1. 加载Maven文件，在pom.xml点击右上方图标自动装载（**必须**）
2. 修改src/main/resources/application.yml中的mysql数据库信息，启动mysql（**必须**）
3. 安装elasticsearch 7.17.3，点击elasticsearch 7.17.3/bin/elasticsearch.bat启动搜索引擎（**必须**）
4. 安装redis 5.0.14，点击redis/redis-server.exe启动（**必须**）
5. 若需爬虫功能，点击spider/FlaskApp.py启动爬虫（**非必须**）
6. 选择edu/zuel/hahasearch/HahaSearchApplication.java程序并启动（**必须**）


## CMD启动项目
完成idea前4项前置后，执行以下命令
```bash
mvn package -DskipTests
```
```bash
nohup java -jar haha-search-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod &
```