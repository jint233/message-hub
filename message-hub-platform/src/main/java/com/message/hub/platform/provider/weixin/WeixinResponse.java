package com.message.hub.platform.provider.weixin;

import lombok.Data;

/**
 * 微信消息发送响应结果
 *
 * @author admin
 * @date 2025/04/27
 */
@Data
@SuppressWarnings({"SpellCheckingInspection"})
public class WeixinResponse {

    private Long errcode;

    private String errmsg;

    public Boolean success() {
        return errcode == 0L;
    }

}
