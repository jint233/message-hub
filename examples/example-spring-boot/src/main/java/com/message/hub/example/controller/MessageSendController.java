package com.message.hub.example.controller;

import com.message.hub.core.content.SubLine;
import com.message.hub.core.domain.SendResult;
import com.message.hub.platform.context.MessageContext;
import com.message.hub.platform.provider.MessageService;
import com.message.hub.platform.provider.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息发送控制器
 *
 * @author admin
 * @date 2025/04/23
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageSendController {

    private final MessageService messageService;

    @GetMapping("/robot/dingtalk")
    public SendResult dingtalkChannel() {
        return messageService.send(MessageContext.buildMarkdown()
                .addAlias("dingtalk-bot-1")
                .addMessageType(MessageType.DingtalkBot)
                .title("【服务器监控告警】")
                .addLine(SubLine.title("应用服务器", 3))
                .addLine(SubLine.bold("服务器名称：server1"))
                .addLine(SubLine.bold("CPU 使用率：90%"))
                .addLine(SubLine.bold("服务器名称：server2"))
                .addLine(SubLine.bold("CPU 使用率：80%"))
                .addLine(SubLine.text("------------------------"))
                .addLine(SubLine.title("数据库服务器", 3))
                .addLine(SubLine.bold("CPU 使用率：90%"))
                .addDingtalkRobot("to_all_user", true));
    }
}
