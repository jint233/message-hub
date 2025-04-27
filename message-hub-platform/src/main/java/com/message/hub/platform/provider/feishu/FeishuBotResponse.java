package com.message.hub.platform.provider.feishu;

import lombok.Data;

import java.util.Objects;

/**
 * 飞书机器人发送消息响应
 *
 * @author admin
 * @date 2025/04/27
 */
@Data
public class FeishuBotResponse {

    private Long code;

    private Object data;

    private String msg;

    public Boolean success() {
        return Objects.isNull(code) || code == 0L;
    }

}
