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

### 发帖

redis缓存限制用户每小时发帖数量

### 评论

发送评论后采用RocketMQ结合websocket异步给用户发送消息。
对于分布式情况下的一种场景：
- 用户在一台服务器（比如服务器 A）上建立了 WebSocket 连接，sessionMap 中保存了对应的 sessionId 和 WebSocketSession。
- 用户发送评论时，HTTP 请求可能被负载均衡器分发到另一台服务器（比如服务器 B）。
- 服务器 B 的 sessionMap 是本地的，没有服务器 A 上的 sessionId 数据，导致 sessionMap.get(sessionId) 返回 null，消息发送失败。

一种方式是简单的Sticky Session，会话粘滞，通过Nginx配置确保同一个用户的整个生命周期始终路由到同一台服务器。

这里使用第二种方案，用redis共享存储管理会话，一个服务器获取到请求后，就首先尝试从自己这里查有没有用户的websocket链接。如果没有，就转发到目标服务器。

