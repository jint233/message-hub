package com.message.hub.core.result;

import com.message.hub.core.common.Constant;
import lombok.Builder;
import lombok.Data;

/**
 * 各平台消息发送响应
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

    public static PlatformSendResult fail(final String message) {
        return PlatformSendResult.builder()
                .code(Constant.DEFAULT_ERROR_CODE)
                .success(false)
                .message(message)
                .build();
    }
}
