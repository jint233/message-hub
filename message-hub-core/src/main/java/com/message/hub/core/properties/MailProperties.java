package com.message.hub.core.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 邮件消息配置
 *
 * @author admin
 * @date 2025/04/22
 */
@Data
public class MailProperties {

    /** 邮箱 SMTP（SMTP）配置 */
    private List<MailProperties.Smtp> smtp;

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Smtp extends MessageChannel {
        private String host;
        private String from;
        private String username;
        private String password;
    }
}
