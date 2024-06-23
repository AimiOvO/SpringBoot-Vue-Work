# 基于springboot+vue的物业管理

## 配置环境
+ **开发工具** VSCode
+ **JDK**       openjdk 1.8.0_372
+ **MySQL**     8.4
+ **Node.js**   v12.22.12
+ **Maven**     3.9.6
+ **Nginx**     1.26.1    
+ **Natapp**    2.4.0
***
## 项目构建
### SpringBoot 后端部署
修改项目配置文件 **gyhr-api\gyhr-base-parent\gyhr-base-web\src\main\resources\application-test.yml**
```yml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://MySQL地址:端口/db_gyhr?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: MySQL用户名
    password: MySQL密码
...
alipay:
  appId: 支付宝沙箱AppId
  appPrivateKey: 支付宝沙箱私钥
  alipayPublicKey: 支付宝沙箱公钥
  notifyUrl: http://natapp代理地址/api/alipay/notify
```
在**MySQL**中创建 **db_gyhr** 数据库，运行 **db_gyhr.sql** 文件载入数据。
导入 **gyhr-base-parent** 到 **VSCode** 或 **IDEA** 构建运行或打包项目。
### Vue 前端部署
安装好 **Node.js v12.22.12**，高版本会报错。
在 **gyhr_web** 中使用 **npm** 构建运行项目。
```cmd
> npm install           //安装项目依赖
> npm run dev           //运行项目
```
Vue 项目打包，打包文件在 **gyhr-web\dist**，可以使用Nginx部署。
```cmd
> npm run build:prod    //打包Vue项目
```
***
