package com.message.hub.platform.provider;

import com.google.common.base.Preconditions;
import com.message.hub.core.domain.PlatformSendResult;
import com.message.hub.core.domain.SendResult;
import com.message.hub.core.properties.MessageChannel;
import com.message.hub.platform.context.MessageContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 消息服务
 *
 * @author admin
 * @date 2025/04/22
 */
@Slf4j
public class MessageService {

    private final CopyOnWriteArrayList<MessageChannel> channels;

    private final ThreadPoolTaskExecutor taskExecutor;

    public MessageService(final List<MessageChannel> channels,
                          final ThreadPoolTaskExecutor taskExecutor) {
        this.channels = new CopyOnWriteArrayList<>(channels);
        this.taskExecutor = taskExecutor;
    }

    /**
     * 发送请求内容
     *
     * @param context 请求上下文，包含别名和消息类型等信息
     * @return 发送结果列表，列表中可能包含成功发送的消息或发送失败的错误信息
     */
    @SuppressWarnings("unchecked")
    public SendResult send(final MessageContext<?> context) {
        final List<MessageChannel> sendChannels = channels.parallelStream()
                .filter(channel -> context.getAlias().isEmpty() ||
                        context.getAlias().contains(channel.getAlias()))
                .filter(channel -> context.getMessageType().isEmpty() ||
                        context.getMessageType().contains(MessageType.getMessageType(channel)))
                .toList();
        final CompletableFuture<PlatformSendResult>[] futures = sendChannels.stream()
                .map(channel ->
                        CompletableFuture.supplyAsync(() -> MessageDispatcher.run(channel, context), taskExecutor))
                .toArray(CompletableFuture[]::new);
        final List<PlatformSendResult> platformResults = CompletableFuture.allOf(futures)
                .thenApply(v -> Arrays.stream(futures).map(CompletableFuture::join).toList())
                .join();
        return SendResult.warp(platformResults);
    }

    /**
     * 添加一个新的消息渠道到列表中。如果列表中已经存在具有相同别名的消息渠道，则先移除该消息渠道，然后添加新的消息渠道。
     *
     * @param channel 要添加的消息渠道，包含消息的别名等信息。
     */
    public synchronized void addChannel(final MessageChannel channel) {
        Preconditions.checkNotNull(channel, "channel must not be null");
        channels.stream()
                .filter(e -> e.getAlias().equals(channel.getAlias()))
                .findAny()
                .ifPresent(channels::remove);
        channels.add(channel);
    }

    /**
     * 根据别名移除渠道，从别名集合中查找与给定别名相匹配的对象，如果找到，则从集合中移除该对象。
     *
     * @param alias 别名 用于查找和移除消息渠道
     */
    public synchronized void removeChannel(final String alias) {
        Preconditions.checkNotNull(alias, "alias must not be null");
        channels.stream()
                .filter(e -> alias.equals(e.getAlias()))
                .findAny()
                .ifPresent(channels::remove);
    }

}
