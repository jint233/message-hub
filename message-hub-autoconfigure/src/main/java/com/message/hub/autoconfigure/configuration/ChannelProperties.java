package com.message.hub.autoconfigure.configuration;

import com.message.hub.core.properties.DingtalkProperties;
import com.message.hub.core.properties.FeishuProperties;
import com.message.hub.core.properties.MailProperties;
import com.message.hub.core.properties.WeixinProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 消息服务属性配置
 *
 * @author jint233
 * @date 2025/04/22
 */
@Data
@ConfigurationProperties(prefix = "message.hub")
public class ChannelProperties {

    /** 钉钉 */
    @NestedConfigurationProperty
    private DingtalkProperties dingtalk;

    /** 飞书 */
    @NestedConfigurationProperty
    private FeishuProperties feishu;

    /** 微信 */
    @NestedConfigurationProperty
    private WeixinProperties weixin;

    /** 邮件 */
    @NestedConfigurationProperty
    private MailProperties mail;

}
