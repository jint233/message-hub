package com.message.hub.core.exception;

/**
 * 邮件消息异常
 *
 * @author admin
 * @date 2025/04/22
 */
public class MailMessageException extends RuntimeException {

    public MailMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
