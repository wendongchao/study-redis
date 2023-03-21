package com.lock.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @auther: dongchao
 * @data: 2023/3/21 11:43
 */
@SpringBootApplication
public class LockRedisApplication {
    public static void main(String[] args) {
        SpringApplication.run(LockRedisApplication.class,args);
    }
}
