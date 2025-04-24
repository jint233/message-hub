package com.message.hub.platform.provider.weixin;

import com.message.hub.core.exception.WeixinMessageException;
import com.message.hub.core.properties.WeixinProperties;
import com.message.hub.platform.context.MarkdownContext;
import com.message.hub.platform.context.TextContext;
import com.message.hub.platform.provider.AbstractSendService;
import com.message.hub.platform.util.ContentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * 微信机器人服务实现
 *
 * @author admin
 * @date 2025/04/22
 */
@Slf4j
public class WeixinBotMessageService extends AbstractSendService<WeixinProperties.Bot> {

    /**
     * 使用markdown格式发送消息到微信自定义机器人
     *
     * @param bot     用于配置微信自定义机器人的别名属性，包含关键的访问令牌
     * @param context 要发送的markdown内容
     * @return 返回从微信服务器接收到的响应信息
     */
    @Override
    public String sendMarkdown(WeixinProperties.Bot bot, MarkdownContext context) {
        Assert.notNull(bot.getKey(), "key should not be null!");
        try {
            // @formatter:off
            // 构建向微信自定义机器人发送的消息请求，其中消息类型设定为markdown，内容为传入的markdown格式文本
            return WeixinUtils.weixinBot(
                    bot.getKey(),
                    WeixinUtils.RobotRequest
                            .builder("markdown")
                            .withMarkdown(ContentUtils.toMarkdown(context))
                            .build()
            );
            // @formatter:on
        } catch (Exception e) {
            log.error("weixin bot markdown message send error", e);
            throw new WeixinMessageException(e.getMessage(), e);
        }
    }

    /**
     * 发送文本消息到微信自定义机器人
     *
     * @param bot     用于配置微信自定义机器人的别名属性，包含关键的访问令牌
     * @param context 要发送的文本内容
     * @return 发送结果的字符串反馈
     */
    @Override
    public String sendText(WeixinProperties.Bot bot, TextContext context) {
        Assert.notNull(bot.getKey(), "key should not be null!");
        try {
            // @formatter:off
            // 构建并发送自定义机器人文本消息
            return WeixinUtils.weixinBot(
                    // 使用别名属性中的关键令牌
                    bot.getKey(),
                    WeixinUtils.RobotRequest
                            // 创建消息请求构建器,指定消息类型为文本
                            .builder("text")
                            // 设置文本消息内容
                            .withText(context.getText())
                            // 构建消息请求
                            .build()
            );
            // @formatter:on
        } catch (Exception e) {
            log.error("weixin bot text message send error", e);
            throw new WeixinMessageException(e.getMessage(), e);
        }
    }
}
