package com.message.hub.platform.provider.dingtalk;

import com.alibaba.fastjson2.JSON;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.message.hub.core.domain.PlatformSendResult;
import com.message.hub.core.exception.DingtalkMessageException;
import com.message.hub.core.properties.DingtalkProperties;
import com.message.hub.platform.context.MarkdownContext;
import com.message.hub.platform.context.TextContext;
import com.message.hub.platform.provider.AbstractSendService;
import com.message.hub.platform.util.ContentUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * 钉钉消息服务实现
 *
 * @author admin
 * @date 2025/04/22
 */
@Slf4j
public class DingtalkChatMessageService extends AbstractSendService<DingtalkProperties.Chat> {

    /**
     * 发送 Markdown 消息到钉钉
     *
     * @param chat    钉钉配置
     * @param context 消息上下文
     * @return {@link PlatformSendResult } 处理结果
     */
    @Override
    public PlatformSendResult sendMarkdown(DingtalkProperties.Chat chat, MarkdownContext context) {
        Map<String, Object> params = context.getContentParams(chat.getAlias()).getDingtalkChat();
        OapiMessageCorpconversationAsyncsendV2Request request = this.request(chat.getAgentId(), params);
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("markdown");
        OapiMessageCorpconversationAsyncsendV2Request.Markdown markdown = new OapiMessageCorpconversationAsyncsendV2Request.Markdown();
        markdown.setTitle(context.getTitle());
        markdown.setText(ContentUtils.toMarkdown(context));
        msg.setMarkdown(markdown);
        request.setMsg(msg);
        return execute(chat, request);
    }

    /**
     * 发送文本消息到钉钉
     *
     * @param chat    钉钉配置
     * @param context 消息上下文
     * @return {@link PlatformSendResult } 处理结果
     */
    @Override
    public PlatformSendResult sendText(DingtalkProperties.Chat chat, TextContext context) {
        Map<String, Object> params = context.getContentParams(chat.getAlias()).getDingtalkChat();
        OapiMessageCorpconversationAsyncsendV2Request request = this.request(chat.getAgentId(), params);
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("text");
        OapiMessageCorpconversationAsyncsendV2Request.Text text = new OapiMessageCorpconversationAsyncsendV2Request.Text();
        text.setContent(context.getText());
        msg.setText(text);
        request.setMsg(msg);
        return execute(chat, request);
    }

    private OapiMessageCorpconversationAsyncsendV2Request request(Long agentId, Map<String, Object> params) {
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(agentId);
        request.setToAllUser(params.containsKey("to_all_user") ? (Boolean) params.get("to_all_user") : null);
        request.setUseridList(params.containsKey("userid_list") ? (String) params.get("userid_list") : null);
        request.setDeptIdList(params.containsKey("dept_id_list") ? (String) params.get("dept_id_list") : null);
        return request;
    }

    /**
     * 执行钉钉消息发送
     * 该方法封装了向钉钉发送消息的逻辑，通过调用钉钉开放平台的API，将请求参数转换为JSON字符串返回
     * 如果在执行过程中遇到异常，将抛出自定义的DingtalkMessageException异常
     *
     * @param chat    钉钉配置属性，包含应用的appKey和appSecret，用于身份验证
     * @param request 发送消息的请求对象，包含了消息的接收者、内容等信息
     * @return {@link PlatformSendResult } 处理结果
     *
     * @throws DingtalkMessageException 如果执行过程中发生异常，将抛出此运行时异常
     */
    private PlatformSendResult execute(DingtalkProperties.Chat chat,
                                       OapiMessageCorpconversationAsyncsendV2Request request) {
        try {
            final OapiMessageCorpconversationAsyncsendV2Response response = DingtalkUtils.getChatClient()
                    .execute(request,
                            DingtalkUtils.getToken(Objects.requireNonNull(chat.getAppKey()),
                                    Objects.requireNonNull(chat.getAppSecret())));
            return PlatformSendResult.builder()
                    .alias(chat.getAlias())
                    .success(response.isSuccess())
                    .code(response.getErrorCode())
                    .message(response.getErrmsg())
                    .detail(JSON.toJSONString(response))
                    .build();
        } catch (Exception e) {
            log.error("dingtalk chat message send error", e);
            throw new DingtalkMessageException(e.getMessage(), e);
        }
    }
}
