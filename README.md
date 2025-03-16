# CreeperHub

## 我的世界苦力怕社区

## Todo


## 项目介绍（SpringBoot）

### 注册

#### 验证码

对于验证码的发送，我在redis中存储了请求验证码用户的IP，
对每分钟固定IP的可尝试发送验证码次数做了限制，防止恶意往他人邮箱发送验证码。

### 登录

同样采用redis限制每个IP每小时的可尝试登录次数

### session
采用session+token方式进行权限校验，redis中存储。
用登录拦截器来对需要校验的接口进行token校验后才放行，并且更新token。
通过token可以实现登出其他设备的功能。

### 用户信息
用put请求更新用户信息，用get请求获取用户信息。
同时使用布隆过滤器处理缓存穿透，用redis缓存用户信息，减轻数据库压力。

![img](https://article.biliimg.com/bfs/new_dyn/83f1b88fe1e6439e5af42f2da3bae47e39684091.png)

