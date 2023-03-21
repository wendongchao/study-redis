package com.base.redis.pubsub.service;

import dai.samples.redis.pubsub.config.RedisListenerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author daify
 * @date 2019-07-25 9:59
 **/
@Component
public class PubSend {

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 发布消息到通道
     *
     * @param message
     */
    public void sendTopicMessage(String message) {
        redisTemplate.convertAndSend(RedisListenerConfig.CHANNEL,
                message);
    }
    
}
