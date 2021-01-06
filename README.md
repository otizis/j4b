# java for blog
simple blog by java.

一点点css，几个文件，看看能不能搭起来写几篇文章。
- [x] 基于springboot，单文件部署，依赖jdk环境即可启动
- [x] 自带评论功能
- [x] 使用所见所得编辑器，tinymce
- [x] 自带图床功能
- [x] 文章数据保存在H2数据库文件中
- [x] 文章标签管理
- [x] 第三方引图（参考Ghost，使用Unsplash，需要配置开发者id
- [x] flyway管理表结构版本

## 运行配置项列表
    unsplash_appid #unsplash 开发者id
    unsplash_proxy #代理地址替换 https://images.unsplash.com
## 部署
- 安装jdk环境
- 下载dist中的jar包文件 blog-x.jar
- 执行命令启动，默认8080端口

        nohup java -jar blog-x.jar & 
- 会在home目录生成 h2.db 数据库文件
- 中止进程，找到pid，kill -9 pid 即可。
## 部署配置项
- server.port=8080 端口
- fileupload.path=D:\\Resource\\MyProject\\j4b\\upload\\ 上传文件保存目录需要有读写权限
- spring.datasource.url=jdbc:h2:file:~/h2 h2数据库文件的目录名称
- 其他可查看/src/main/resource/application.properties文件
- 可以在启动命令指定，前面加--

        nohup java -jar blog-x.jar --server.port=80 & 