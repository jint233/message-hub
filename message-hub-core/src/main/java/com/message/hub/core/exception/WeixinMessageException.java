package com.message.hub.core.exception;

/**
 * 微信消息异常
 *
 * @author admin
 * @date 2025/04/22
 */
public class WeixinMessageException extends RuntimeException {

    public WeixinMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
