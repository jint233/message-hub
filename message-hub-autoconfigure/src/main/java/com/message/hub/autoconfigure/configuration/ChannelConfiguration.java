package com.message.hub.autoconfigure.configuration;

import com.message.hub.core.exception.MessageException;
import com.message.hub.core.properties.MessageChannel;
import com.message.hub.platform.provider.MessageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 消息服务配置
 *
 * @author jint233
 * @date 2025/04/22
 */
@Configuration
@EnableConfigurationProperties({ChannelProperties.class})
public class ChannelConfiguration {

    /**
     * 核心线程数 = cpu 核心数 + 1
     */
    private final int core = Runtime.getRuntime().availableProcessors() + 1;

    /**
     * 线程池配置
     *
     * @return {@link ThreadPoolTaskExecutor } 消息发送自定义线程池
     */
    @Bean(name = "messageHubTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(core);
        executor.setMaxPoolSize(core * 2);
        executor.setQueueCapacity(2000);
        executor.setKeepAliveSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("message-hub-thread-");
        return executor;
    }

    /**
     * 创建并初始化消息服务
     * 这个方法根据配置属性来决定使用哪个消息记录服务（IMessageRecordService）
     * 并聚合各种消息通道（如钉钉、飞书、微信、邮件等）的配置信息，用于之后的消息发送服务
     *
     * @param properties 消息配置属性，包含各种消息通道的配置信息，以及指定的消息记录服务名称
     * @return 初始化后的消息服务实例，用于发送消息和记录消息发送情况
     *
     * @throws MessageException 如果根据配置属性未能找到对应的消息记录服务实例，则抛出异常
     */
    @Bean
    @ConditionalOnMissingBean(MessageService.class)
    public MessageService init(@Qualifier("messageHubTaskExecutor") final ThreadPoolTaskExecutor executor,
                               final ChannelProperties properties) {
        // 集合所有消息别名，包括钉钉、飞书、微信、邮件等通道的消息配置
        final List<MessageChannel> channels = new ArrayList<>();
        if (Objects.nonNull(properties.getDingtalk())) {
            this.safeAdd(channels, properties.getDingtalk().getBot());
            this.safeAdd(channels, properties.getDingtalk().getChat());
        }
        if (Objects.nonNull(properties.getFeishu())) {
            this.safeAdd(channels, properties.getFeishu().getBot());
            this.safeAdd(channels, properties.getFeishu().getChat());
        }
        if (Objects.nonNull(properties.getWeixin())) {
            this.safeAdd(channels, properties.getWeixin().getBot());
            this.safeAdd(channels, properties.getWeixin().getChat());
        }
        if (Objects.nonNull(properties.getMail())) {
            this.safeAdd(channels, properties.getMail().getSmtp());
        }
        final Set<String> aliases = new HashSet<>((int) (channels.size() / 0.75 + 1));
        for (MessageChannel channel : channels) {
            if (!aliases.add(channel.getAlias())) {
                throw new MessageException("消息通道别名重复: " + channel.getAlias());
            }
        }
        return new MessageService(channels, executor);
    }

    private <T extends MessageChannel> void safeAdd(List<MessageChannel> all, List<T> channels) {
        if (!CollectionUtils.isEmpty(channels)) {
            all.addAll(channels);
        }
    }
}
