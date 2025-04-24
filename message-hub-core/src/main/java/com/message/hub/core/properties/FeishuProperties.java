package com.message.hub.core.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 飞书消息配置
 *
 * @author admin
 * @date 2025/04/22
 */
@Data
public class FeishuProperties {

    /** 飞书机器人 */
    private List<FeishuProperties.Bot> bot;

    /** 飞书消息 */
    private List<FeishuProperties.Chat> chat;

    /**
     * 飞书机器人
     * <a href="https://open.feishu.cn/document/client-docs/bot-v3/add-custom-bot"> 自定义机器人使用指南 </a>
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Bot extends MessageChannel {
        private String hookId;
        private String secret;
    }

    /**
     * 飞书消息
     * <a href="https://open.feishu.cn/document/server-docs/im-v1/message/create"> 发送消息 </a>
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Chat extends MessageChannel {
        private String appId;
        private String appSecret;
    }
}
