package com.message.hub.core.domain;

import lombok.Builder;
import lombok.Data;

/**
 * 发送响应
 *
 * @author jint233
 * @date 2025/04/27
 */
@Data
@Builder
public class PlatformSendResult {

    private Boolean success;

    private String code;

    private String message;

    private String detail;

    private String alias;

}
