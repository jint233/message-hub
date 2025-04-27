package com.message.hub.platform.provider.mail;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * 邮件消息工具类
 *
 * @author jint233
 * @date 2025/04/22
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class MailUtils {

    /**
     * 使用SMTP协议发送邮件的函数
     *
     * @param host     邮件服务器主机名或地址
     * @param from     发件人邮箱地址
     * @param to       收件人邮箱地址
     * @param username 发件人邮箱用户名（通常为邮箱地址）
     * @param password 发件人邮箱密码
     * @param title    邮件主题（如果为空，则不设置主题，邮件内容将作为纯文本发送）
     * @param body     邮件正文（支持HTML格式）
     * @throws MessagingException 如果邮件操作出现异常，则抛出此异常
     */
    public static void smtp(String host, String from, String to, String username, String password, String title, String body) throws MessagingException {
        // 1. 创建参数配置，用于连接邮件服务器的参数配置
        Properties props = new Properties();
        // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.transport.protocol", "smtp");
        // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.host", host);
        // 需要请求认证
        props.setProperty("mail.smtp.auth", "true");

        // 2. 根据配置创建会话对象，用于和邮件服务器交互
        Session session = Session.getInstance(props);
        // 设置为debug模式,可以查看详细的发送log
        session.setDebug(true);

        // 3. 构造邮件内容
        MimeMessage message = new MimeMessage(session);
        message.setRecipients(Message.RecipientType.TO, to);
        if (StringUtils.hasText(title)) {
            message.setSubject(title);
            // 设置邮件内容为HTML格式
            message.setContent(body, "text/html;charset=UTF-8");
        } else {
            // 如果没有主题，则将邮件内容设置为纯文本
            message.setText(body);
        }
        message.setFrom(new InternetAddress(from));

        // 4. 根据 Session获取邮件传输对象
        Transport transport = session.getTransport();
        // 使用用户名和密码连接邮件服务器
        transport.connect(username, password);
        // 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
    }

}
