package com.message.hub.platform.provider.feishu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.message.hub.core.content.SubBoldLine;
import com.message.hub.core.content.SubLinkLine;
import com.message.hub.core.util.RestClientUtils;
import com.message.hub.platform.context.MarkdownContext;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 飞书消息工具类
 *
 * @author admin
 * @date 2025/04/22
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class FeishuUtils {

    private static final String WEBHOOK = "https://open.feishu.cn/open-apis/bot/v2/hook/%s";

    /**
     * 向定制机器人发送请求
     *
     * @param hookId 机器人的hookId，用于标识特定的机器人
     * @param body   发送给机器人的消息体
     * @return 返回从机器人接收到的响应内容
     */
    public static String feishuBot(String hookId, BotRequest body) {
        // 使用RestClientUtils的post方法，向指定webhook地址发送post请求
        return RestClientUtils.post(String.format(WEBHOOK, hookId), body);
    }

    /**
     * 将Markdown内容转换为飞书机器人可识别的Post内容详情列表
     *
     * @param content Markdown内容对象，包含多行文本数据。
     * @return 返回一个FeishuUtils.BotRequest.PostContentDetail对象的列表，其中每个对象代表飞书消息中的一个元素（如文本、链接等）
     */
    public static List<BotRequest.PostContentDetail> toPost(MarkdownContext content) {
        // 遍历Markdown内容的每一行，并根据行类型转换为飞书消息格式
        return content.getLines().stream().map(line ->
                switch (line.getLineType()) {
                    // 当行为链接类型时
                    case LINK -> {
                        SubLinkLine subLinkLine = (SubLinkLine) line;
                        // 转换为飞书消息中的链接元素
                        yield new BotRequest.PostContentDetail("a", subLinkLine.getContent(), subLinkLine.getLink(), null, null);
                    }
                    // 当行为文本、标题、引用或加粗类型时
                    case TEXT, TITLE, QUOTE, BOLD -> {
                        SubBoldLine subLinkLine = (SubBoldLine) line;
                        // 转换为飞书消息中的文本元素
                        yield new BotRequest.PostContentDetail("text", subLinkLine.getContent(), null, null, null);
                    }
                }).toList();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BotRequest {
        private @JsonProperty("msg_type") String msgType;
        private @JsonProperty("content") Object content;
        private @JsonProperty("timestamp") Long timeStamp;
        private @JsonProperty("sign") String sign;

        public BotRequest(String msgType, Long timeStamp, String sign) {
            this.msgType = msgType;
            this.timeStamp = timeStamp;
            this.sign = sign;
        }

        public static Builder builder(String msgType, Long timeStamp, String sign) {
            return new Builder(msgType, timeStamp, sign);
        }

        public static class Builder {
            BotRequest options;

            public Builder(String msgType, Long timeStamp, String sign) {
                this.options = new BotRequest(msgType, timeStamp, sign);
            }

            public Builder withPost(String title, List<PostContentDetail> content) {
                Assert.isTrue(!"markdown" .equals(this.options.msgType), " msgType value is not markdown, should not use this method!");
                this.options.content = new PostContent(new Post(new ZhCn(title, content)));
                return this;
            }

            public Builder withPost(List<PostContentDetail> content) {
                return withPost(null, content);
            }

            public Builder withText(TextContent text) {
                Assert.isTrue(!"text" .equals(this.options.msgType), " msgType value is not text, should not use this method!");
                this.options.content = text;
                return this;
            }

            public BotRequest build() {
                return this.options;
            }

        }

        public record TextContent(String text) {
        }

        public record PostContent(Post post) {
        }

        public record Post(@JsonProperty("zh_cn") ZhCn zhCn) {
        }

        public record ZhCn(String title, List<PostContentDetail> content) {
        }

        public record PostContentDetail(String tag,
                                        String text,
                                        String href,
                                        @JsonProperty("user_id") String userId,
                                        @JsonProperty("user_name") String userName) {
        }
    }
}
