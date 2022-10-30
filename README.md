# myDemo

# 技术栈

## 后端技术栈

后端主要采用了：

1. SpringBoot  
2. SpringSecurity  
3. MyBatis  
4. 部分接口遵循Restful风格  
5. MySQL

## 前端技术栈

前端主要采用了：

1. Vue  
2. axios  
3. ElementUI  
4. vue-echarts  
5. maven-editor  
6. vue-router

还有其他一些琐碎的技术我就不在这里一一列举了。

# 快速运行

1. 克隆本项目到本地
```
git@github.com:fmz200/myDemo.git
```
2. 找到myDemo项目中resources目录下的vueblog.sql文件，在MySQL数据库中执行  
3. 根据自己本地情况修改数据库配置，数据库配置在myDemo项目的application-devDb.yml中，为了区分本地开发环境和发布服务器的环境，大部分配置都分开了dev和prod，具体启动哪个环境在application.yml文件中修改
```yml
  #旧的配置方式
  profiles:
    active: dev
```
4. 在IntelliJ IDEA中运行myDemo项目

**OK，至此，服务端就启动成功了，此时我们直接在地址栏输入 http://localhost:8081/index.html 即可访问项目，如果要做二次开发，请继续看第5、6步。**

5. 用前端开发工具（WebStorm，VSCODE等）打开myDemo-web，进入到项目根目录中，在命令行依次输入如下命令：

```
# 安装依赖
npm install

# 在 localhost:8080 启动项目
npm run dev
```  

由于我在myDemo-web项目中已经配置了端口转发，将数据转发到myDemo上，因此项目启动之后，在浏览器中输入 http://localhost:8080 就可以访问我们的前端项目了，所有的请求通过端口转发将数据传到SpringBoot中（注意此时不要关闭SpringBoot项目）。

6. 开发完成后，当项目要上线时，进入到myDemo-web根目录，然后执行如下命令：

```
npm run build
```  

该命令执行成功之后，myDemo-web目录下生成一个dist文件夹，将该文件夹中的两个文件static和index.html拷贝到myDemo项目中resources/static/目录下，然后就可以像第4步那样直接访问了。


**步骤5中需要大家对NodeJS、NPM等有一定的使用经验，不熟悉的小伙伴可以先自行搜索学习下，推荐[Vue官方教程](https://cn.vuejs.org/v2/guide/)。**


# 项目依赖

1.[vue-echarts](https://github.com/Justineo/vue-echarts)  
2.[mavonEditor](https://github.com/hinesboy/mavonEditor)

# License

MIT

## 其他

Forked from https://github.com/lenve/VBlog ，进行二次开发，前后端分离成了两个项目。

# 项目效果图

## 登陆页面

![登录](https://raw.githubusercontent.com/fmz200/myDemo/master/myDemo-web/doc/login.png)

## 文章列表

![文章列表](https://raw.githubusercontent.com/fmz200/myDemo/master/myDemo-web/doc/article.png)

## 发表文章

![发表文章](https://raw.githubusercontent.com/fmz200/myDemo/master/myDemo-web/doc/post.png)

## 用户管理

![用户管理](https://raw.githubusercontent.com/fmz200/myDemo/master/myDemo-web/doc/usermana.png)

## 栏目管理

![栏目管理](https://raw.githubusercontent.com/fmz200/myDemo/master/myDemo-web/doc/category.png)

## 数据统计

![数据统计](https://raw.githubusercontent.com/fmz200/myDemo/master/myDemo-web/doc/datastatistics.png)
