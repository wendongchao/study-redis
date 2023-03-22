package com.redission.redis.config;


import cn.hutool.extra.spring.SpringUtil;
import com.redission.redis.service.RedisDelayedQueueListener;
import com.redission.redis.utils.RedisDelayQueueEnum;
import com.redission.redis.utils.RedisDelayQueueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * <p>
 *  启动延迟队列监测扫描,会在服务启动之后被立即执行。
 * <p>
 *
 * @author Zaki Chen
 * @version 1.0
 * @since 2023-03-08
 **/


@Slf4j
@Component
public class RedisDelayQueueRunner implements CommandLineRunner {

    @Resource
    private RedisDelayQueueUtil redisDelayQueueUtil;


    @Override
    public void run(String... args) {
        RedisDelayQueueEnum[] queueEnums = RedisDelayQueueEnum.values();

        for (RedisDelayQueueEnum queueEnum : queueEnums) {
            new Thread(() -> {
                try {
                    while (true) {
                        // 线程阻塞等待。
                        Object value = redisDelayQueueUtil.get(queueEnum.getCode());
                        RedisDelayedQueueListener RedisDelayedQueueListener = SpringUtil.getBean(queueEnum.getBeanId());
                        RedisDelayedQueueListener.execute(value);
                    }
                } catch (InterruptedException e) {
                    log.error("Redis延迟队列异常中断====> {}", e.getMessage());
                }
            }).start();
        }
        log.info("Redis延迟队列启动成功");
    }
}