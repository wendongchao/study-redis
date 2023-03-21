package com.base.redis.pubsub.config;

import dai.samples.redis.pubsub.listener.MyRedisMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * redis的监听配置
 * @author daify
 * @date 2019-07-25 9:55
 **/
@Configuration
public class RedisListenerConfig {
    /**
     * 通道名称
     */
    public static String CHANNEL = "dai.channel";

    @Bean 
    RedisMessageListenerContainer container(
            RedisConnectionFactory connectionFactory,
            MyRedisMessageListener listener) {
        // 新建监听对象
        RedisMessageListenerContainer container = 
                new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 订阅了一个通道
        container.addMessageListener(listener, new PatternTopic(CHANNEL));
        // 这个container 可以添加多个 messageListener
        return container;
    }
    
}
