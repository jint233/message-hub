package com.message.hub.platform.context;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文参数
 *
 * @author jint233
 * @date 2025/04/22
 */
@Data
public class ContextParams {

    /** 钉钉机器人 */
    private Map<String, Object> dingtalkBot = new HashMap<>();

    /** 钉钉消息 */
    private Map<String, Object> dingtalkChat = new HashMap<>();

    /** 飞书机器人 */
    private Map<String, Object> feishuBot = new HashMap<>();

    /** 飞书消息 */
    private Map<String, Object> feishuChat = new HashMap<>();

    /** 企业微信机器人 */
    private Map<String, Object> weixinBot = new HashMap<>();

    /** 企业微信消息 */
    private Map<String, Object> weixinChat = new HashMap<>();

    /** 邮件 SMTP */
    private Map<String, Object> mailSmtp = new HashMap<>();
}
