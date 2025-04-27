package com.message.hub.example;

import com.message.hub.autoconfigure.annotation.EnableMessageHub;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 应用程序示例
 *
 * @author jint233
 * @date 2025/04/23
 */
@EnableMessageHub
@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

}
