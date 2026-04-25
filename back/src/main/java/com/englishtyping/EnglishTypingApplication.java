package com.englishtyping;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 英语学习打字网站后端启动类
 * @author lei
 */
@EnableFileStorage
@SpringBootApplication
public class EnglishTypingApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnglishTypingApplication.class, args);
    }
}
