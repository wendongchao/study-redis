package com.example.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @auther: dongchao
 * @data: 2023/3/12 15:18
 */
@EnableCaching// 开启缓存功能 https://www.macrozheng.com/mall/reference/spring_data_redis.html#%E5%B8%B8%E7%94%A8%E6%B3%A8%E8%A7%A3
@SpringBootApplication
public class RedisApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class,args);
    }
}
