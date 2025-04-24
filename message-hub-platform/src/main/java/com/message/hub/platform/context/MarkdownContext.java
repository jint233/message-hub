package com.message.hub.platform.context;

import com.message.hub.core.content.SubLine;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Markdown 消息上下文
 *
 * @author admin
 * @date 2025/04/22
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MarkdownContext extends MessageContext<MarkdownContext> {

    List<SubLine> lines = new ArrayList<>();
    private String title;

    public MarkdownContext title(String title) {
        this.title = title;
        return this;
    }

    public MarkdownContext addLine(SubLine line) {
        this.lines.add(line);
        return this;
    }
}
