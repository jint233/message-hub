package com.message.hub.platform.provider.weixin;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.message.hub.core.domain.PlatformSendResult;
import com.message.hub.core.properties.WeixinProperties;
import com.message.hub.core.util.CaffeineCache;
import com.message.hub.core.util.RestClientUtils;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

/**
 * 微信消息工具类
 *
 * @author jint233
 * @date 2025/04/22
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class WeixinUtils {

    private static final String WEBHOOK = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s";
    private static final String GET_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    private static final String MESSAGE = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";

    /**
     * 获取企业微信的访问令牌
     *
     * @param corpId     企业ID，用于标识企业微信账号
     * @param corpSecret 企业微信的密钥，用于验证身份
     * @return 返回从企业微信服务器获取的访问令牌
     */
    private static String getToken(String corpId, String corpSecret) {
        // 尝试从缓存中获取令牌
        String token = CaffeineCache.getToken("weixin-" + corpId);
        if (token == null) {
            // 如果缓存中未找到令牌，则通过网络请求从企业微信服务器获取
            WeixinToken weixinToken = RestClientUtils.get(String.format(GET_TOKEN, corpId, corpSecret), WeixinToken.class);
            Assert.isTrue(weixinToken.errCode() != 0, "get weixin token is failed! message: " + weixinToken.errMessage);
            // 将获取到的令牌及其过期时间存入缓存
            token = CaffeineCache.setToken("weixin-" + corpId, weixinToken.expiresIn(), weixinToken.accessToken());
        }
        return token;
    }

    /**
     * 向自定义机器人发送请求
     * 该方法通过POST请求向指定的Webhook地址发送消息体
     *
     * @param bot  机器人配置
     * @param body 要发送的消息体内容
     * @return {@link PlatformSendResult } 响应结果
     */
    public static PlatformSendResult botSend(WeixinProperties.Bot bot, WeixinUtils.RobotRequest body) {
        final String response = RestClientUtils.post(String.format(WEBHOOK, bot.getKey()), body);
        final WeixinResponse botResponse = JSON.parseObject(response, WeixinResponse.class);
        return PlatformSendResult.builder()
                .alias(bot.getAlias())
                .success(botResponse.success())
                .code(String.valueOf(botResponse.getErrcode()))
                .message(botResponse.getErrmsg())
                .detail(response)
                .build();
    }


    /**
     * 发送企业消息
     *
     * @param chat 消息渠道配置
     * @param body 消息的内容，将被发送给指定的企业
     * @return {@link PlatformSendResult } 响应结果
     */
    public static PlatformSendResult chatSend(WeixinProperties.Chat chat, MessageRobotRequest body) {
        final String response = RestClientUtils.post(String.format(MESSAGE, getToken(chat.getCorpId(), chat.getCorpSecret())), body);
        final WeixinResponse chatResponse = JSON.parseObject(response, WeixinResponse.class);
        return PlatformSendResult.builder()
                .alias(chat.getAlias())
                .success(chatResponse.success())
                .code(String.valueOf(chatResponse.getErrcode()))
                .message(chatResponse.getErrmsg())
                .detail(response)
                .build();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RobotRequest {

        private @JsonProperty("msgtype") String msgType;
        private @JsonProperty("markdown") Content markdown;
        private @JsonProperty("text") Content text;

        public RobotRequest(String msgType) {
            this.msgType = msgType;
        }

        public static Builder builder(String msgType) {
            return new Builder(msgType);
        }

        public static class Builder {
            RobotRequest options;

            public Builder(String msgType) {
                this.options = new RobotRequest(msgType);
            }

            public Builder withMarkdown(String markdown) {
                Assert.isTrue(!"markdown".equals(this.options.msgType), " msgType value is not markdown, should not use this method!");
                this.options.markdown = new Content(markdown);
                return this;
            }

            public Builder withText(String text) {
                Assert.isTrue(!"text".equals(this.options.msgType), " msgType value is not text, should not use this method!");
                this.options.text = new Content(text);
                return this;
            }

            public RobotRequest build() {
                return this.options;
            }
        }

        public record Content(String content) {
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MessageRobotRequest {
        private @JsonProperty("msgtype") String msgType;
        private @JsonProperty("agentid") String agentId;
        private @JsonProperty("markdown") Content markdown;
        private @JsonProperty("text") Content text;
        private @JsonProperty("touser") String toUser;
        private @JsonProperty("toparty") String toParty;
        private @JsonProperty("totag") String toTag;

        public MessageRobotRequest(String msgType, String agentId) {
            this.msgType = msgType;
            this.agentId = agentId;
        }

        public static Builder builder(String msgType, String agentId) {
            return new Builder(msgType, agentId);
        }

        public static class Builder {
            MessageRobotRequest options;

            public Builder(String msgType, String agentId) {
                this.options = new MessageRobotRequest(msgType, agentId);
            }

            public Builder withMarkdown(String markdown) {
                Assert.isTrue(!"markdown".equals(this.options.msgType), " msgType value is not markdown, should not use this method!");
                this.options.markdown = new Content(markdown);
                return this;
            }

            public Builder withText(String text) {
                Assert.isTrue(!"text".equals(this.options.msgType), " msgType value is not text, should not use this method!");
                this.options.text = new Content(text);
                return this;
            }

            public Builder withToUser(String toUser) {
                this.options.toUser = toUser;
                return this;
            }

            public Builder withToParty(String toParty) {
                this.options.toParty = toParty;
                return this;
            }

            public Builder withToTag(String toTag) {
                this.options.toTag = toTag;
                return this;
            }

            public MessageRobotRequest build() {
                return this.options;
            }
        }

        public record Content(String content) {
        }
    }

    public record WeixinToken(
            // 出错返回码，为0表示成功，非0表示调用失败
            @JsonProperty("errcode") Integer errCode,
            // 返回码提示语
            @JsonProperty("errcode") String errMessage,
            // 	获取到的凭证，最长为512字节
            @JsonProperty("access_token") String accessToken,
            // 	凭证的有效时间（秒）
            @JsonProperty("expires_in") Long expiresIn) {
    }
}
