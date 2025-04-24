package com.message.hub.platform.provider;

import com.message.hub.core.exception.MessageException;
import com.message.hub.core.properties.MessageChannel;
import com.message.hub.platform.provider.dingtalk.DingtalkBotMessageService;
import com.message.hub.platform.provider.dingtalk.DingtalkChatMessageService;
import com.message.hub.platform.provider.feishu.FeishuBotMessageService;
import com.message.hub.platform.provider.feishu.FeishuChatMessageService;
import com.message.hub.platform.provider.mail.MailSmtpMessageService;
import com.message.hub.platform.provider.weixin.WeixinBotMessageService;
import com.message.hub.platform.provider.weixin.WeixinChatMessageService;
import lombok.Getter;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

/**
 * 消息类型
 *
 * @author admin
 * @date 2025/04/22
 */
public enum MessageType {

    /** 钉钉定制机器人 */
    DingtalkBot("DingtalkProperties$Bot"),

    /** 钉钉消息 */
    DingtalkChat("DingtalkProperties$Chat"),

    /** 飞书定制机器人 */
    FeishuBot("FeishuProperties$Bot"),

    /** 飞书消息 */
    FeishuChat("FeishuProperties$Chat"),

    /** 微信定制机器人 */
    WeixinBot("WeixinProperties$Bot"),

    /** 微信消息 */
    WeixinChat("WeixinProperties$Chat"),

    /** 邮件 SMTP */
    MailSmtp("MailProperties$Smtp");

    private static final Map<MessageType, Class<? extends ISendService<? extends MessageChannel>>> MESSAGE_TYPE_MAPPER = new EnumMap<>(MessageType.class);
    private static final String PROPERTY_BASE_PACKAGE = "com.message.hub.core.properties.";

    static {
        MESSAGE_TYPE_MAPPER.put(MessageType.DingtalkBot, DingtalkBotMessageService.class);
        MESSAGE_TYPE_MAPPER.put(MessageType.DingtalkChat, DingtalkChatMessageService.class);
        MESSAGE_TYPE_MAPPER.put(MessageType.FeishuBot, FeishuBotMessageService.class);
        MESSAGE_TYPE_MAPPER.put(MessageType.FeishuChat, FeishuChatMessageService.class);
        MESSAGE_TYPE_MAPPER.put(MessageType.WeixinBot, WeixinBotMessageService.class);
        MESSAGE_TYPE_MAPPER.put(MessageType.WeixinChat, WeixinChatMessageService.class);
        MESSAGE_TYPE_MAPPER.put(MessageType.MailSmtp, MailSmtpMessageService.class);
    }

    @Getter
    private final String className;

    MessageType(String className) {
        this.className = className;
    }

    public static MessageType getMessageType(MessageChannel channel) {
        return Arrays.stream(MessageType.values())
                .filter(type -> channel.getClass().getName().equals(PROPERTY_BASE_PACKAGE + type.getClassName()))
                .findAny()
                .orElseThrow(() -> new MessageException("未知的消息配置！"));
    }

    @SuppressWarnings("unchecked")
    public static <T extends ISendService<R>, R extends MessageChannel> Class<T> getClass(R channel) {
        return (Class<T>) MESSAGE_TYPE_MAPPER.get(getMessageType(channel));
    }
}
