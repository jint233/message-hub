package com.message.hub.core.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 钉钉消息配置
 *
 * @author jint233
 * @date 2025/04/22
 */
@Data
public class DingtalkProperties {

    /** 钉钉机器人 */
    private List<DingtalkProperties.Bot> bot;

    /** 钉钉消息 */
    private List<DingtalkProperties.Chat> chat;

    /**
     * 钉钉机器人
     * <a href="https://open.dingtalk.com/document/group/custom-robot-access"> 自定义机器人接入 </a>
     * <a href="https://open.dingtalk.com/document/orgapp/custom-bot-to-send-group-chat-messages"> 自定义机器人发送群聊消息 </a>
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Bot extends MessageChannel {
        private String accessToken;
        private String secret;
    }

    /**
     * 钉钉消息
     * <a href="https://open.dingtalk.com/document/orgapp/asynchronous-sending-of-enterprise-session-messages"> 发送工作通知 </a>
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Chat extends MessageChannel {
        private String appKey;
        private String appSecret;
        private Long agentId;
    }
}
