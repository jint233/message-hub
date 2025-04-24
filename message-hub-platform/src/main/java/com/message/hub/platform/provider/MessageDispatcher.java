package com.message.hub.platform.provider;

import com.message.hub.core.properties.MessageChannel;
import com.message.hub.core.exception.MessageException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息调度器
 *
 * @author admin
 * @date 2025/04/22
 */
@Slf4j
@SuppressWarnings({"unchecked", "rawtypes"})
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class MessageDispatcher {

    private static final ConcurrentHashMap<Class, ISendService> INSTANCES = new ConcurrentHashMap<>();

    public static <T extends ISendService<R>, R extends MessageChannel> String run(R channel, Object context) {
        return RetryTemplate.builder()
                .maxAttempts(3)
                .exponentialBackoff(1000, 2, 5000)
                .retryOn(Exception.class)
                .build()
                .execute(ctx -> {
                    Class<T> clazz = MessageType.getClass(channel);
                    ISendService sendService = INSTANCES.computeIfAbsent(clazz, key -> {
                        try {
                            return clazz.getConstructor().newInstance();
                        } catch (Exception e) {
                            throw new MessageException("服务初始化失败: " + clazz.getName());
                        }
                    });
                    return sendService.send(channel, context);
                });
    }

}
