package com.message.hub.platform.provider;

import com.message.hub.platform.context.MarkdownContext;
import com.message.hub.platform.context.TextContext;
import com.message.hub.core.properties.MessageChannel;
import com.message.hub.core.exception.MessageException;

/**
 * 消息发送服务抽象类
 *
 * @author admin
 * @date 2025/04/22
 */
public abstract class AbstractSendService<T extends MessageChannel> implements ISendService<T> {

    @Override
    public String send(T channel, Object context) {
        if (context instanceof MarkdownContext mc) {
            return this.sendMarkdown(channel, mc);
        }
        if (context instanceof TextContext tc) {
            return this.sendText(channel, tc);
        }
        throw new MessageException("未知的消息内容类型!");
    }
}
