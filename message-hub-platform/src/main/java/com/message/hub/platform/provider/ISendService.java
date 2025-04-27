package com.message.hub.platform.provider;

import com.message.hub.core.domain.PlatformSendResult;
import com.message.hub.core.properties.MessageChannel;
import com.message.hub.platform.context.MarkdownContext;
import com.message.hub.platform.context.TextContext;

/**
 * 消息发送服务
 *
 * @author admin
 * @date 2025/04/22
 */
public interface ISendService<T extends MessageChannel> {

    /**
     * 消息发送
     *
     * @param channel 消息通道属性配置
     * @param context 消息上下文
     * @return {@link PlatformSendResult } 消息发送结果
     */
    PlatformSendResult send(T channel, Object context);

    /**
     * Markdown消息发送
     *
     * @param channel 消息通道属性配置
     * @param context 消息上下文
     * @return {@link PlatformSendResult } 消息发送结果
     */
    PlatformSendResult sendMarkdown(T channel, MarkdownContext context);

    /**
     * 文本消息发送
     *
     * @param channel 消息通道属性配置
     * @param context 消息上下文
     * @return {@link PlatformSendResult } 消息发送结果
     */
    PlatformSendResult sendText(T channel, TextContext context);
}
