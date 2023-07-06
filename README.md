# haha-search-back
哈哈搜索后端(基于spring全家桶、mybatis-pro)

## idea启动项目
1. 加载Maven文件
2. 修改application中的数据库信息
3. 选择edu/zuel/hahasearch/HahaSearchApplication.java程序启动

## CMD启动项目
```bash
mvn package -DskipTests
```
```bash
nohup java -jar haha-search-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod &
```
