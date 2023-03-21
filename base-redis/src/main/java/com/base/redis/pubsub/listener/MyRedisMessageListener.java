package com.base.redis.pubsub.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author daify
 * @date 2019-07-25 10:13
 **/
@Slf4j
@Component
public class MyRedisMessageListener implements MessageListener {


    @Autowired
    private RedisTemplate <String, String> redisTemplate;
    /**
     * 接收消息的处理
     * Callback for processing received objects through Redis.
     *  @param message message must not be {@literal null}.
     * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
     */
    @Override 
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();
        String msgContent = 
                (String) redisTemplate
                        .getValueSerializer()
                        .deserialize(body);
        String topic = 
                redisTemplate
                        .getStringSerializer()
                        .deserialize(channel);
        log.info("redis--topic:" + topic + "  body:" + msgContent);
    }
}
