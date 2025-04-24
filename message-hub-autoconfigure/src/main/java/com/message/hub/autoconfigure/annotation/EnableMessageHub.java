package com.message.hub.autoconfigure.annotation;

import com.message.hub.autoconfigure.configuration.ChannelConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用聚合消息发送服务
 *
 * @author admin
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ChannelConfiguration.class})
public @interface EnableMessageHub {
}
