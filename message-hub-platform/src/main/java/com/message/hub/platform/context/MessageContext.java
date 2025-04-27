package com.message.hub.platform.context;

import com.message.hub.platform.provider.MessageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * 消息上下文
 *
 * @author jint233
 * @date 2025/04/22
 */
@Getter
@SuppressWarnings({"unchecked"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageContext<T> {

    protected List<String> alias = new ArrayList<>();
    protected List<MessageType> messageType = new ArrayList<>();
    protected ContextParams params = new ContextParams();
    protected Map<String, ContextParams> aliasParams = new HashMap<>();

    public static TextContext buildText() {
        return new TextContext();
    }

    public static MarkdownContext buildMarkdown() {
        return new MarkdownContext();
    }

    public T addAlias(String... alias) {
        Collections.addAll(this.alias, alias);
        return (T) this;
    }

    public T addMessageType(MessageType... messageType) {
        Collections.addAll(this.messageType, messageType);
        return (T) this;
    }

    public T addDingtalkRobot(String key, Object value) {
        params.getDingtalkBot().put(key, value);
        return (T) this;
    }

    public T addDingtalkMessage(String key, Object value) {
        params.getDingtalkChat().put(key, value);
        return (T) this;
    }

    public T addFeishuRobot(String key, Object value) {
        params.getFeishuBot().put(key, value);
        return (T) this;
    }

    public T addFeishuMessage(String key, Object value) {
        params.getFeishuChat().put(key, value);
        return (T) this;
    }

    public T addWeixinMessage(String key, Object value) {
        params.getWeixinChat().put(key, value);
        return (T) this;
    }

    public T addMailSmtp(String key, Object value) {
        params.getMailSmtp().put(key, value);
        return (T) this;
    }

    public T addDingtalkRobot(String alias, String key, Object value) {
        aliasParams.computeIfAbsent(alias, s -> new ContextParams()).getDingtalkBot().put(key, value);
        return (T) this;
    }

    public T addDingtalkMessage(String alias, String key, Object value) {
        aliasParams.computeIfAbsent(alias, s -> new ContextParams()).getDingtalkChat().put(key, value);
        return (T) this;
    }

    public T addFeishuRobot(String alias, String key, Object value) {
        aliasParams.computeIfAbsent(alias, s -> new ContextParams()).getFeishuBot().put(key, value);
        return (T) this;
    }

    public T addFeishuMessage(String alias, String key, Object value) {
        aliasParams.computeIfAbsent(alias, s -> new ContextParams()).getFeishuChat().put(key, value);
        return (T) this;
    }

    public T addWeixinMessage(String alias, String key, Object value) {
        aliasParams.computeIfAbsent(alias, s -> new ContextParams()).getWeixinChat().put(key, value);
        return (T) this;
    }

    public T addMailSmtp(String alias, String key, Object value) {
        aliasParams.computeIfAbsent(alias, s -> new ContextParams()).getMailSmtp().put(key, value);
        return (T) this;
    }

    public ContextParams getContentParams(String alias) {
        if (aliasParams.containsKey(alias)) {
            return aliasParams.get(alias);
        }
        return params;
    }
}
