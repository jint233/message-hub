package com.message.hub.core.exception;

/**
 * 飞书消息异常
 *
 * @author admin
 * @date 2025/04/22
 */
public class FeishuMessageException extends RuntimeException {

    public FeishuMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
