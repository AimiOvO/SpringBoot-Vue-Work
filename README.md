# 基于springboot+vue的物业管理
***
~~毕业设计的**最大难点**在于**盖章**~~。
***
## 已实现项目功能
前后端分离，使用SpringSecurity完成项目权限管理。
### 管理员功能
+ **系统管理**
  - 员工管理
  - 部门管理
  - 权限管理
+ **房屋管理**
  - 楼栋列表
  - 单元列表
  - 房屋列表
+ **设施管理**
  - 车位管理
+ **租凭管理**
  - 租户管理
  - 租房单管理
  - 租车位管理
+ **房屋管理**
  - 电费管理
  - 水费管理
  - 租凭费管理
  - 停车费管理
+ **平台服务**
  - 投诉管理
  - 维修管理
  - 公告管理
### 租户功能
+ **我的缴费**
  - 缴费功能
+ **平台服务**
  - 我的投诉
  - 我的维修
### 维修人员功能
+ **平台服务**
  - 待处理维修
### 待完成功能
+ **图片删除**
***
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
