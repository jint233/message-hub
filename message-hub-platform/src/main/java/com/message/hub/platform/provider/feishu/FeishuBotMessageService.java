package com.message.hub.platform.provider.feishu;

import com.message.hub.core.domain.PlatformSendResult;
import com.message.hub.core.exception.FeishuMessageException;
import com.message.hub.core.properties.FeishuProperties;
import com.message.hub.platform.context.MarkdownContext;
import com.message.hub.platform.context.TextContext;
import com.message.hub.platform.provider.AbstractSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 飞书机器人服务实现
 *
 * @author admin
 * @date 2025/04/22
 */
@Slf4j
public class FeishuBotMessageService extends AbstractSendService<FeishuProperties.Bot> {

    /**
     * 发送Markdown消息到飞书机器人
     *
     * @param bot     飞书机器人配置属性
     * @param context Markdown消息上下文
     * @return {@link PlatformSendResult } 响应结果
     */
    @Override
    public PlatformSendResult sendMarkdown(FeishuProperties.Bot bot, MarkdownContext context) {
        // 将Markdown内容转换为飞书机器人可接受的格式列表
        List<FeishuUtils.BotRequest.PostContentDetail> list = FeishuUtils.toPost(context);

        // 为消息添加@别名的功能
        context.getContentParams(bot.getAlias()).getFeishuBot()
                .forEach((key, value) ->
                        list.add(new FeishuUtils.BotRequest.PostContentDetail("at", null, null, key, (String) value)));

        // 获取当前时间戳，用于消息签名
        Long timestamp = System.currentTimeMillis();
        try {
            // 构建并发送消息到飞书机器人
            return FeishuUtils.feishuBot(bot,
                    FeishuUtils.BotRequest
                            .builder("post", timestamp, sign(timestamp, bot.getSecret()))
                            .withPost(context.getTitle(), list)
                            .build()
            );
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("feishu bot markdown message send error", e);
            throw new FeishuMessageException(e.getMessage(), e);
        }
    }


    /**
     * 发送文本消息到飞书机器人
     *
     * @param bot     飞书机器人的配置属性
     * @param context 文本消息上下文
     * @return {@link PlatformSendResult } 响应结果
     *
     * @throws FeishuMessageException 如果在签名算法处理过程中发生异常，则抛出此运行时异常
     */
    @Override
    public PlatformSendResult sendText(FeishuProperties.Bot bot, TextContext context) {
        Long timestamp = System.currentTimeMillis();
        try {
            // 构建并发送自定义机器人消息请求，其中包含消息的文本内容。
            return FeishuUtils.feishuBot(bot,
                    FeishuUtils.BotRequest.builder("text", timestamp, sign(timestamp, bot.getSecret()))
                            .withText(
                                    new FeishuUtils.BotRequest.TextContent(
                                            context.getContentParams(bot.getAlias()).getFeishuBot()
                                                    .entrySet()
                                                    .stream()
                                                    .map(entry -> "<at user_id = \"" + entry.getKey() + "\">" + entry.getValue() + "</at>").
                                                    collect(Collectors.joining(" "))
                                                    + " "
                                                    + context.getText())
                            ).build()
            );
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("feishu bot text message send error", e);
            throw new FeishuMessageException(e.getMessage(), e);
        }
    }

    /**
     * 使用HmacSHA256算法对给定的时间戳和密钥进行签名
     *
     * @param timestamp 需要签名的时间戳，单位为毫秒
     * @param secret    用于签名的密钥
     * @return 返回经过Base64编码的签名数据字符串
     *
     * @throws NoSuchAlgorithmException 当指定的算法不可用时抛出该异常
     * @throws InvalidKeyException      当密钥无效时抛出该异常
     */
    private String sign(Long timestamp, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        // 构建待签名字符串，将时间戳和密钥以换行符分隔
        String stringToSign = timestamp + "\n" + secret;

        // 初始化HmacSHA256消息认证码对象
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));

        // 计算签名
        byte[] signData = mac.doFinal(new byte[]{});

        // 将签名数据使用Base64编码成字符串后返回
        return new String(Base64.encodeBase64(signData));
    }
}
