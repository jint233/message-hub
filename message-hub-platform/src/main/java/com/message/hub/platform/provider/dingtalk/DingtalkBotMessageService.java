package com.message.hub.platform.provider.dingtalk;

import com.alibaba.fastjson2.JSON;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.message.hub.core.exception.DingtalkMessageException;
import com.message.hub.core.properties.DingtalkProperties;
import com.message.hub.platform.context.MarkdownContext;
import com.message.hub.platform.context.TextContext;
import com.message.hub.platform.provider.AbstractSendService;
import com.message.hub.platform.util.ContentUtils;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 钉钉机器人服务实现
 *
 * @author admin
 * @date 2025/04/22
 */
@Slf4j
public class DingtalkBotMessageService extends AbstractSendService<DingtalkProperties.Bot> {

    /**
     * 发送 Markdown 消息到钉钉机器人
     *
     * @param bot     钉钉机器人配置
     * @param context 消息上下文
     * @return {@link String } 处理结果
     */
    @Override
    public String sendMarkdown(DingtalkProperties.Bot bot, MarkdownContext context) {
        Map<String, Object> params = context.getContentParams(bot.getAlias()).getDingtalkRobot();
        OapiRobotSendRequest request = this.request(params);
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(context.getTitle());
        markdown.setText(ContentUtils.toMarkdown(context));
        request.setMarkdown(markdown);
        return execute(bot, request);
    }

    /**
     * 发送文本消息到钉钉机器人
     *
     * @param bot     钉钉机器人配置
     * @param context 消息上下文
     * @return {@link String } 处理结果
     */
    @Override
    public String sendText(DingtalkProperties.Bot bot, TextContext context) {
        Map<String, Object> params = context.getContentParams(bot.getAlias()).getDingtalkRobot();
        OapiRobotSendRequest request = this.request(params);
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(context.getText());
        request.setText(text);
        return execute(bot, request);
    }

    @SuppressWarnings("unchecked")
    private OapiRobotSendRequest request(Map<String, Object> params) {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setIsAtAll(params.containsKey("isAll") ? (Boolean) params.get("isAll") : null);
        at.setAtMobiles(params.containsKey("atMobiles") ? (List<String>) params.get("atMobiles") : null);
        at.setAtUserIds(params.containsKey("atUserIds") ? (List<String>) params.get("atUserIds") : null);
        request.setAt(at);
        return request;
    }

    /**
     * 执行钉钉自定义机器人发送消息的操作
     * 该方法封装了向钉钉自定义机器人发送消息的过程，通过提供的access-token和secret验证身份，并发送请求
     * 如果在执行过程中出现异常，将会抛出DingtalkMessageException
     *
     * @param bot     钉钉自定义机器人配置属性，包含access-token和secret
     * @param request 发送给钉钉自定义机器人的消息请求对象
     * @return 返回钉钉服务器响应的JSON字符串
     *
     * @throws DingtalkMessageException 如果执行过程中出现异常，将会抛出此运行时异常
     */
    private String execute(DingtalkProperties.Bot bot, OapiRobotSendRequest request) {
        try {
            // @formatter:off
            // 使用JSON.toJSONString序列化钉钉客户端执行结果为JSON字符串
            return JSON.toJSONString(
                    DingtalkUtils
                            .getBotClient(
                                    Objects.requireNonNull(bot.getAccessToken()),
                                    Objects.requireNonNull(bot.getSecret()))
                            .execute(request)
            );
            // @formatter:on
        } catch (ApiException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("dingtalk bot message send error", e);
            throw new DingtalkMessageException(e.getMessage(), e);
        }
    }
}
