package com.message.hub.platform.provider.feishu;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.lark.oapi.Client;
import com.lark.oapi.core.request.RequestOptions;
import com.lark.oapi.core.utils.Lists;
import com.lark.oapi.service.im.v1.model.CreateMessageReq;
import com.lark.oapi.service.im.v1.model.CreateMessageReqBody;
import com.lark.oapi.service.im.v1.model.CreateMessageResp;
import com.message.hub.core.exception.FeishuMessageException;
import com.message.hub.core.properties.FeishuProperties;
import com.message.hub.platform.context.MarkdownContext;
import com.message.hub.platform.context.MessageContext;
import com.message.hub.platform.context.TextContext;
import com.message.hub.platform.provider.AbstractSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 飞书消息服务实现
 *
 * @author admin
 * @date 2025/04/22
 */
@Slf4j
public class FeishuChatMessageService extends AbstractSendService<FeishuProperties.Chat> {

    private static final String RECEIVE_ID_TYPE = "receive_id_type";

    /**
     * 发送Markdown消息到飞书
     *
     * @param chat    飞书消息配置属性
     * @param context Markdown内容对象，包含消息标题和内容
     * @return 返回发送消息后的响应结果，格式为JSON字符串
     */
    @Override
    public String sendMarkdown(FeishuProperties.Chat chat, MarkdownContext context) {
        Assert.notNull(chat.getAppId(), "appId should not be null!");
        Assert.notNull(chat.getAppSecret(), "appSecret should not be null!");

        // 构建消息体，包含标题和内容
        JSONObject jo = new JSONObject();
        JSONObject post = new JSONObject();
        post.put("title", context.getTitle());
        post.put("content", new JSONArray(FeishuUtils.toPost(context)));
        jo.put("zh_cn", post);

        try {
            CreateMessageResp resp = this.execute(chat, context, jo, "post");
            return JSON.toJSONString(resp);
        } catch (Exception e) {
            log.error("feishu chat markdown message send error", e);
            throw new FeishuMessageException(e.getMessage(), e);
        }
    }

    /**
     * 发送文本消息到飞书
     *
     * @param chat    飞书消息配置属性
     * @param context 消息内容，包括文本和额外参数
     * @return 发送结果，成功返回JSON格式的响应信息，失败返回错误信息字符串
     */
    @Override
    public String sendText(FeishuProperties.Chat chat, TextContext context) {
        Assert.notNull(chat.getAppId(), "appId should not be null!");
        Assert.notNull(chat.getAppSecret(), "appSecret should not be null!");

        // 构造消息体，包含文本内容
        JSONObject jo = new JSONObject();
        jo.put("text", context.getText());

        try {
            CreateMessageResp resp = this.execute(chat, context, jo, "text");
            return JSON.toJSONString(resp);
        } catch (Exception e) {
            log.error("feishu chat text message send error", e);
            throw new FeishuMessageException(e.getMessage(), e);
        }
    }

    /**
     * 发送IM消息到飞书（用户/群组）
     *
     * @param chat    飞书配置属性
     * @param context 消息上下文
     * @param obj     消息内容对象
     * @param type    消息类型
     * @return {@link CreateMessageResp } 发送结果
     *
     * @throws Exception 发送异常
     */
    private CreateMessageResp execute(FeishuProperties.Chat chat, MessageContext<?> context, JSONObject obj, String type) throws Exception {
        // 创建飞书客户端
        Client client = Client.newBuilder(chat.getAppId(), chat.getAppSecret()).build();

        // 设置请求头，包含消息接收者类型
        Map<String, List<String>> headers = new HashMap<>(16);
        headers.put(RECEIVE_ID_TYPE, Lists.newArrayList((String) context.getContentParams(chat.getAlias()).getFeishuMessage().get(RECEIVE_ID_TYPE)));

        // 创建并发送消息，获取响应
        return client.im()
                .message()
                .create(
                        CreateMessageReq.newBuilder()
                                .createMessageReqBody(
                                        CreateMessageReqBody.newBuilder()
                                                .receiveId((String) context.getParams().getFeishuMessage().get("receive_id"))
                                                .msgType(type)
                                                .content(obj.toJSONString())
                                                .build()
                                )
                                .build(),
                        RequestOptions.newBuilder()
                                .headers(headers)
                                .build()
                );
    }

}
