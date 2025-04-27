package com.message.hub.platform.util;

import com.message.hub.core.content.SubBoldLine;
import com.message.hub.core.content.SubLinkLine;
import com.message.hub.core.content.SubQuoteLine;
import com.message.hub.core.content.SubTitleLine;
import com.message.hub.platform.context.MarkdownContext;
import lombok.NoArgsConstructor;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 消息内容工具类
 *
 * @author jint233
 * @date 2025/04/22
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ContentUtils {

    public static String toMarkdown(MarkdownContext content) {
        return toMarkdown(content, "\n\n");
    }

    /**
     * 将MarkdownContent内容转换为Markdown格式
     *
     * @param content MarkdownContent内容对象，包含多行不同类型的SubLine内容
     * @param join    连接符，用于行与行之间的连接
     * @return 转换后的Markdown字符串
     */
    public static String toMarkdown(MarkdownContext content, String join) {
        // 使用流处理每行内容，并根据行的类型转换为相应的Markdown格式
        return content.getLines().stream().map(line ->
                switch (line.getLineType()) {
                    // 根据标题级别生成相应的#号和内容
                    case TITLE -> {
                        SubTitleLine subTitleLine = (SubTitleLine) line;
                        yield IntStream.range(0, subTitleLine.getLevel())
                                .mapToObj(i -> "#")
                                .collect(Collectors.joining()) + " " + subTitleLine.getContent();
                    }
                    // 生成Markdown的链接格式
                    case LINK -> {
                        SubLinkLine subLinkLine = (SubLinkLine) line;
                        yield String.format("[%s](%s)", subLinkLine.getContent(), subLinkLine.getLink());
                    }
                    // 生成Markdown的引用格式
                    case QUOTE -> {
                        SubQuoteLine subQuoteLine = (SubQuoteLine) line;
                        yield String.format("> %s", subQuoteLine.getContent());
                    }
                    // 生成Markdown的加粗格式
                    case BOLD -> {
                        SubBoldLine subBoldLine = (SubBoldLine) line;
                        yield String.format("**%s**", subBoldLine.getContent());
                    }
                    // 默认情况下直接返回内容
                    default -> line.getContent();
                }).collect(Collectors.joining(join));
    }

    /**
     * 将Markdown内容转换为HTML格式
     *
     * @param content MarkdownContent对象，包含待转换的Markdown文本
     * @return String 返回转换后的HTML字符串
     */
    public static String toHTML(MarkdownContext content) {
        // 构建Markdown解析器并使用它来解析Markdown文本，然后将解析结果渲染为HTML
        return HtmlRenderer.builder()
                .build()
                .render(Parser.builder().build().parse(toMarkdown(content, "  ")));
    }
}
