package com.message.hub.platform.provider.mail;

import com.message.hub.core.domain.PlatformSendResult;
import com.message.hub.core.exception.MailMessageException;
import com.message.hub.core.properties.MailProperties;
import com.message.hub.platform.context.MarkdownContext;
import com.message.hub.platform.context.TextContext;
import com.message.hub.platform.provider.AbstractSendService;
import com.message.hub.platform.util.ContentUtils;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * 邮件消息服务实现
 *
 * @author admin
 * @date 2025/04/22
 */
@Slf4j
public class MailSmtpMessageService extends AbstractSendService<MailProperties.Smtp> {

    /**
     * 使用Markdown格式发送邮件
     *
     * @param smtp    邮件配置属性
     * @param context 邮件内容上下文
     * @return {@link PlatformSendResult } 响应结果
     */
    @Override
    public PlatformSendResult sendMarkdown(MailProperties.Smtp smtp, MarkdownContext context) {
        // 根据别名获取邮件接收者地址，将Markdown内容转换为HTML格式，并发送邮件
        return this.mail(
                smtp,
                (String) context.getContentParams(smtp.getAlias()).getMailSmtp().get("to"),
                context.getTitle(),
                ContentUtils.toHTML(context)
        );
    }

    /**
     * 使用SMTP属性和文本内容发送电子邮件
     * <p>
     * 该方法通过SMTP属性和文本内容来发送电子邮件。它首先从文本内容中提取收件人地址
     * 然后调用mail方法来实际发送邮件。此方法特别适用于只需要发送纯文本邮件的场景
     *
     * @param smtp    邮件配置属性
     * @param context 邮件内容上下文
     * @return {@link PlatformSendResult } 响应结果
     */
    @Override
    public PlatformSendResult sendText(MailProperties.Smtp smtp, TextContext context) {
        // 从context中获取邮件的收件人地址，并使用smtp和邮件内容调用mail方法发送邮件
        return this.mail(
                smtp,
                (String) context.getContentParams(smtp.getAlias()).getMailSmtp().get("to"),
                null,
                context.getText()
        );
    }

    /**
     * 使用SMTP方式发送邮件的函数
     *
     * @param smtp    包含SMTP服务器相关属性，不能为null，其中必须包含host（SMTP服务器地址）、from（发件人地址）、username（发件人账号）、password（发件人密码）等信息
     * @param to      收件人邮箱地址，不能为null
     * @param title   邮件主题，不能为null
     * @param content 邮件正文，不能为null
     * @return {@link PlatformSendResult } 响应结果
     *
     * @throws MailMessageException 如果邮件发送过程中出现任何MessagingException异常，将封装并抛出MailMessageException
     */
    public PlatformSendResult mail(MailProperties.Smtp smtp, String to, String title, String content) {
        // 校验必须的参数信息
        Assert.notNull(smtp.getHost(), "host should not be null!");
        Assert.notNull(smtp.getFrom(), "from should not be null!");
        Assert.notNull(to, "to should not be null!");
        Assert.notNull(smtp.getUsername(), "username should not be null!");
        Assert.notNull(smtp.getPassword(), "password should not be null!");

        try {
            // 尝试通过SMTP方式发送邮件
            MailUtils.smtp(
                    smtp.getHost(),
                    smtp.getFrom(),
                    to,
                    smtp.getUsername(),
                    smtp.getPassword(),
                    title, content
            );
            return PlatformSendResult.builder()
                    .alias(smtp.getAlias())
                    .success(true)
                    .code("0")
                    .message("success")
                    .build();
        } catch (MessagingException e) {
            log.error("mail smtp message send error", e);
            throw new MailMessageException(e.getMessage(), e);
        }
    }
}
