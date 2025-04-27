package com.message.hub.core.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 微信消息配置
 *
 * @author admin
 * @date 2025/04/22
 */
@Data
public class WeixinProperties {

    /** 微信机器人 */
    private List<WeixinProperties.Bot> bot;

    /** 微信消息 */
    private List<WeixinProperties.Chat> chat;

    /**
     * 自定义机器人
     * <a href="https://developer.work.weixin.qq.com/document/path/99110"> 群机器人配置说明 </a>
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Bot extends MessageChannel {
        private String key;
    }

    /**
     * 发送应用消息
     * <a href="https://developer.work.weixin.qq.com/document/path/90236"> 发送应用消息 </a>
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Chat extends MessageChannel {
        private String corpId;
        private String corpSecret;
        private String agentId;
    }
}
