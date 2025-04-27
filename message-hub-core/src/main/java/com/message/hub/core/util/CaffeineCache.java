package com.message.hub.core.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Caffeine 缓存
 *
 * @author jint233
 * @date 2025/04/22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CaffeineCache {

    private static final String TOKEN = "token";
    private static final ConcurrentMap<String, Cache<Object, Object>> CACHES = new ConcurrentHashMap<>();

    /**
     * 获取指定名称的缓存对象。如果该缓存不存在，则根据提供的秒数创建一个新的缓存对象
     *
     * @param cacheName 缓存的名称，用于标识缓存
     * @param seconds   缓存的过期时间，单位为秒。用于新建缓存时设置其过期时间
     * @return 返回指定名称的缓存对象，如果存在则直接返回，不存在则创建后返回
     */
    public static Cache<Object, Object> getCache(String cacheName, Long seconds) {
        Assert.notNull(cacheName, "cacheName value should bot be null!");
        Assert.notNull(seconds, "seconds value should bot be null!");
        return CACHES.computeIfAbsent(cacheName,
                cache ->
                        Caffeine.newBuilder()
                                .expireAfterWrite(Duration.ofSeconds(seconds))
                                .build()
        );
    }

    /**
     * 获取指定缓存名称的令牌
     *
     * @param cacheName 缓存的名称，用于标识特定的缓存
     * @return 如果缓存中存在指定名称的缓存项，则返回对应的令牌字符串；如果不存在或缓存为空，则返回null
     */
    public static String getToken(String cacheName) {
        if (!CACHES.containsKey(cacheName)) {
            return null;
        }
        return (String) CACHES.get(cacheName).getIfPresent(TOKEN);
    }

    /**
     * 将token设置到指定的缓存中，并返回该token
     *
     * @param cacheName  缓存的名称，不能为null
     * @param tokenValue 要设置的token值，不能为null
     * @return 如果缓存中之前已经存在了token，则返回之前的token值；否则返回null
     */
    public static String setToken(String cacheName, Long seconds, Object tokenValue) {
        Assert.notNull(tokenValue, "token value should bot be null!");
        Cache<Object, Object> cache = getCache(cacheName, seconds);
        cache.put(TOKEN, tokenValue);
        return (String) cache.getIfPresent(TOKEN);
    }
}
