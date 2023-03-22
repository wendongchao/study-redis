package com.redission.redis.config;


import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *  Redisson配置类
 * <p>
 *
 * @author Zaki Chen
 * @version 1.0
 * @since 2023-03-08
 **/

@Slf4j
@Configuration
public class RedissonConfig {

    private final String REDISSON_PREFIX = "redis://";

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.database}")
    private int database;
    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient redissonClient() {

        Config config = new Config();
        String url = REDISSON_PREFIX + host + ":" + port;

        // 单台redis服务器，实际开发中建议使用cluster或者哨兵模式
        if (!StrUtil.isEmpty(password)) {
            config.useSingleServer()
                    .setAddress(url)
                    .setDatabase(database)
                    .setPassword(password)
                    .setPingConnectionInterval(2000); // 心跳检测2秒，默认为30秒
        }else {
            config.useSingleServer()
                    .setAddress(url)
                    .setDatabase(database)
                    .setPingConnectionInterval(2000);
        }



        // 设置锁超时时间为10秒，默认为30秒
        config.setLockWatchdogTimeout(10000L);


        try {
            RedissonClient redissonClient = Redisson.create(config);
            log.info("RedissonClient 初始化成功 ！ redis url:[{}]", url);
            return redissonClient;
        } catch (Exception e) {
            log.error("RedissonClient 初始化失败 ！ redis url:[{}], 异常:", url, e);
            return null;
        }
    }

}
