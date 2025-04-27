package com.message.hub.platform.provider.weixin;

import com.message.hub.core.result.PlatformSendResult;
import com.message.hub.core.exception.WeixinMessageException;
import com.message.hub.core.properties.WeixinProperties;
import com.message.hub.platform.context.MarkdownContext;
import com.message.hub.platform.context.TextContext;
import com.message.hub.platform.provider.AbstractSendService;
import com.message.hub.platform.util.ContentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 微信消息服务实现
 *
 * @author jint233
 * @date 2025/04/22
 */
@Slf4j
public class WeixinChatMessageService extends AbstractSendService<WeixinProperties.Chat> {

    private static final String TOUSER = "touser";
    private static final String TOPARTY = "toparty";
    private static final String TOTAG = "totag";

    /**
     * 发送Markdown格式的消息
     *
     * @param chat    微信配置属性，包含企业ID、企业密钥和别名配置的对象，用于认证和指定消息发送的主体
     * @param context Markdown内容及其发送参数的对象
     * @return {@link PlatformSendResult } 响应结果
     */
    @Override
    public PlatformSendResult sendMarkdown(WeixinProperties.Chat chat, MarkdownContext context) {
        // 确保企业ID和企业密钥不为空
        Assert.notNull(chat.getCorpId(), "corpId should not be null!");
        Assert.notNull(chat.getCorpSecret(), "corpSecret should not be null!");

        // 根据别名获取消息参数
        Map<String, Object> params = context.getContentParams(chat.getAlias()).getWeixinChat();

        try {
            // 构建并发送Markdown消息
            return WeixinUtils.chatSend(chat,
                    WeixinUtils.MessageRobotRequest
                            .builder("markdown", chat.getAgentId())
                            .withMarkdown(ContentUtils.toMarkdown(context))
                            .withToUser((String) params.getOrDefault(TOUSER, null))
                            .withToParty((String) params.getOrDefault(TOPARTY, null))
                            .withToTag((String) params.getOrDefault(TOTAG, null))
                            .build()
            );
        } catch (Exception e) {
            log.error("weixin markdown message send error", e);
            throw new WeixinMessageException(e.getMessage(), e);
        }
    }

    /**
     * 发送文本消息给指定的微信企业号用户、部门或标签
     *
     * @param chat    微信配置属性，包括企业ID、企业密钥和代理ID等信息
     * @param context 文本消息内容，包括消息文本和别名等信息
     * @return {@link PlatformSendResult } 响应结果
     */
    @Override
    public PlatformSendResult sendText(WeixinProperties.Chat chat, TextContext context) {
        // 确保企业ID和企业密钥不为空
        Assert.notNull(chat.getCorpId(), "corpId should not be null!");
        Assert.notNull(chat.getCorpSecret(), "corpSecret should not be null!");

        // 根据别名获取消息参数
        Map<String, Object> params = context.getContentParams(chat.getAlias()).getWeixinChat();

        try {
            // 构建并发送Markdown消息
            return WeixinUtils.chatSend(chat,
                    WeixinUtils.MessageRobotRequest
                            .builder("text", chat.getAgentId())
                            .withText(context.getText())
                            .withToUser((String) params.getOrDefault(TOUSER, null))
                            .withToParty((String) params.getOrDefault(TOPARTY, null))
                            .withToTag((String) params.getOrDefault(TOTAG, null))
                            .build()
            );
        } catch (Exception e) {
            log.error("weixin text message send error", e);
            throw new WeixinMessageException(e.getMessage(), e);
        }
    }

}
