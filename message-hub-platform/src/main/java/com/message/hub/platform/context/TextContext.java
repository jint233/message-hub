package com.message.hub.platform.context;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 文本消息上下文
 *
 * @author jint233
 * @date 2025/04/22
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TextContext extends MessageContext<TextContext> {

    private String text;

    public TextContext text(String text) {
        this.text = text;
        return this;
    }
}
