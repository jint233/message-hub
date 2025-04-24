# message-hub-spring-boot-starter

![MIT](https://img.shields.io/badge/License-Apache2.0-blue.svg) ![JDK](https://img.shields.io/badge/JDK-17+-green.svg) ![SpringBoot](https://img.shields.io/badge/Srping%20Boot-3+-green.svg)

> 这是一个统一消息推送服务
>
通过配置和编码，即可将相同的消息通过钉钉自定义机器人、钉钉消息、飞书自定义机器人、飞书消息、企业微信自定义机器人、企业微信消息以及邮箱消息通道进行发送
> 提供统一消息维护方式，发送时会按照对应的平台类型自动进行转换

## 使用示例

## 第一步 编译源码 install 到本地

## 第二步 引入本地 jar

1.0 版本升级到 jdk 17 SpringBoot 3.2+

```xml

<dependency>
    <groupId>com.message.hub</groupId>
    <artifactId>message-hub-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## 第三步 在启动类上加上`@EnableMessageHub`注解

```java

@EnableMessageHub
@SpringBootApplication
public class ExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}
```

## 第四步 `application.yml`配置文件中添加以下相关配置，可以配置多个群

#### 1. 配置钉钉自定义机器人

> 钉钉自定义机器人的配置请参考
> [自定义机器人接入](https://open.dingtalk.com/document/orgapp/custom-bot-access)
> [自定义机器人安全设置](https://open.dingtalk.com/document/orgapp/customize-bot-security-settings)
> 获得 access_token 和 secret

```yaml
message:
  hub:
    dingtalk:
      bot:
        - alias: alias #所有通道的别名不能重复
          access-token: accessToken
          secret: secret
```

#### 2. 配置钉钉消息

> 钉钉消息需要先注册企业内部应用，应用的配置请参考
> [基础概念](https://open.dingtalk.com/document/isvapp/basic-concepts)
> 获得 appKey、appSecret、agentId

```yaml
message:
  hub:
    dingtalk:
      chat:
        - alias: alias #所有通道的别名不能重复
          app-key: appKey
          app-secret: appSecret
          agent-id: agentId
```

#### 3. 配置飞书自定义机器人

> 飞书自定义机器人的配置请参考
> [自定义机器人使用指南](https://open.feishu.cn/document/client-docs/bot-v3/add-custom-bot)
> 获得 hookId、secret

```yaml
message:
  hub:
    feishu:
      bot:
        - alias: alias #所有通道的别名不能重复
          hook-id: hookId
          secret: secret
```

#### 4. 配置飞书消息

> 飞书消息需要先注册企业内部应用，应用的配置请参考
> [如何获取应用的 App ID](https://open.feishu.cn/document/faq/trouble-shooting/how-to-obtain-app-id)
> 获得 appId、secret

```yaml
message:
  hub:
    feishu:
      chat:
        - alias: alias #所有通道的别名不能重复
          app-id: appId
          app-secret: appSecret
```

#### 5. 企业微信自定义机器人

> 企业微信自定义机器人的配置请参考
> [群机器人配置说明](https://developer.work.weixin.qq.com/document/path/99110)
> 获得 key

```yaml
message:
  hub:
    weixin:
      chat:
        - alias: alias #所有通道的别名不能重复
          key: key
```

#### 6. 企业微信消息

> 企业微信消息需要先注册企业内部应用，应用的配置请参考
> [基本概念介绍](https://developer.work.weixin.qq.com/document/path/90665)
> 获得corpId、corpSecret、agentId

```yaml
message:
  hub:
    weixin:
      chat:
        - alias: alias #所有通道的别名不能重复
          corp-id: corpId
          corp-secret: corpSecret
          agent-id: agentId
```

#### 7. 邮箱

```yaml
message:
  hub:
    mail:
      smtp:
        - alias: alias #所有通道的别名不能重复
          host: host
          from: from
          username: username
          password: password
```

## 第五步 注入 MessageService,编写消息内容，调用发送

#### 1. 编写消息内容

```java
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageSendController {

    private final MessageService messageService;

    @GetMapping("/bot/dingtalk")
    public List<String> dingtalkChannel() {
        return messageService.send(MessageContext.buildMarkdown()
                .addAlias("dingtalk-bot-1")
                .addMessageType(MessageType.DingtalkRobot)
                .title("【服务器监控告警-平台】")
                .addLine(SubLine.title("应用服务器", 3))
                .addLine(SubLine.text("------------------------"))
                .addLine(SubLine.bold("服务器名称：server1"))
                .addLine(SubLine.bold("CPU 使用率：90%"))
                .addLine(SubLine.bold("服务器名称：server2"))
                .addLine(SubLine.bold("CPU 使用率：80%"))
                .addLine(SubLine.text("          "))
                .addLine(SubLine.title("数据库服务器", 3))
                .addLine(SubLine.text("-------------------------"))
                .addLine(SubLine.bold("CPU 使用率：90%"))
                .addDingtalkRobot("to_all_user", true));
    }
}
```

行数据类型与转换格式对照表

| messageChannel | 钉钉           | 微信           | 飞书        | 邮件                  |
|----------------|--------------|--------------|-----------|---------------------|
| SubLine.text   | Markdown: 文字 | Markdown: 文字 | 文本标签：text | html: \<p>          |
| SubLine.title  | Markdown: 标题 | Markdown: 标题 | 文本标签：text | html: \<h1>~\<h6>   |
| SubLine.link   | Markdown: 链接 | Markdown: 链接 | 超链接标签：a   | html: \<a>          |
| SubLine.quote  | Markdown: 引用 | Markdown: 引用 | 文本标签：text | html: \<blockquote> |
| SubLine.bold   | Markdown: 加粗 | Markdown: 加粗 | 文本标签：text | html: \<strong>     |

#### 2.根据别名、消息通道类型配置额外参数

> 在实际项目中使用消息通道时，
> 比如使用钉钉自定义机器人发送群消息时，会出现@所有人或者@某个人的需求
> 使用钉钉消息的时候，也需要指定特定用户
> 这是需要额外的参数协助消息发往正确的目标
> 下面的示例是为钉钉自定义机器人增加 @所有人 参数

```java
MessageContext.buildMarkdown()
        .addDingtalkRobot("isAtAll",Boolean.TRUE) //根据消息通道类型添加参数
        .addDingtalkRobot("dd-1","isAtAll",Boolean.TRUE) //根据消息通道别名添加参数，优先级更大
        .title("测试消息")
        .addLine(SubLine.text("这是一行文本"));
```

###### 1. 钉钉自定义机器人支持参数

| 参数        | 参数类型         | 说明           |
|-----------|--------------|--------------|
| atMobiles | List<String> | 被@人的手机号      |
| atUserIds | List<String> | 被@人的用户userid |
| isAtAll   | Boolean      | 是否@所有人       |

###### 2. 钉钉消息支持参数

| 参数           | 参数类型    | 说明                                                          |
|--------------|---------|-------------------------------------------------------------|
| userid_list  | String  | user123,user456 接收者的userid列表，最大用户列表长度100                    |
| dept_id_list | String  | 123,345 接收者的部门id列表，最大列表长度20。接收者是部门ID时，包括子部门下的所有用户。          |
| to_all_user  | Boolean | 是否发送给企业全部用户(当设置为false时必须指定userid_list或dept_id_list其中一个参数的值) |

###### 3. 飞书机器人支持参数

@所有人
.addFeishuRobot("all","所有人")
@单个用户（填入用户的 Open ID，且必须是有效值，否则取名字展示，并不产生实际的 @ 效果）
.addFeishuRobot("ou_xxx","名字")

###### 4. 飞书消息

| 参数              | 参数类型   | 说明                                               |
|-----------------|--------|--------------------------------------------------|
| receive_id_type | String | 消息接收者id类型 open_id/user_id/union_id/email/chat_id |
| receive_id      | String | 消息接收者的ID，ID类型应与查询参数receive_id_type 对应            |

###### 5. 企业微信自定义机器人

暂无支持的额外参数

###### 6. 企业微信消息

| 参数      | 参数类型   | 说明                                                                     |
|---------|--------|------------------------------------------------------------------------|
| touser  | String | 指定接收消息的成员，成员ID列表（多个接收者用‘\|’分隔，最多支持1000个）。特殊情况：指定为"@all"，则向该企业应用的全部成员发送 |
| toparty | String | 指定接收消息的部门，部门ID列表，多个接收者用‘\|’分隔，最多支持100个。当touser为"@all"时忽略本参数            |
| totag   | String | 指定接收消息的标签，标签ID列表，多个接收者用‘\|’分隔，最多支持100个。当touser为"@all"时忽略本参数            |

###### 7. 邮箱

| 参数 | 参数类型   | 说明          |
|----|--------|-------------|
| to | String | 收件邮箱1,收件邮箱2 |

## 其他1：动态增减平台信息

```java
//可以通过如下方法添加平台信息
messageService.add();

//可以通过如下方法删除平台信息
messageService.

removeByAlias();
```
