package com.message.hub.core.exception;

/**
 * 钉钉消息异常
 *
 * @author jint233
 * @date 2025/04/22
 */
public class DingtalkMessageException extends RuntimeException {

    public DingtalkMessageException(String message, Throwable cause) {
        super(message, cause);
    }

}
