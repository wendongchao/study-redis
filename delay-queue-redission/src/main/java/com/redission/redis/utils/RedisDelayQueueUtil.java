package com.redission.redis.utils;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 *  基于redis+redisson的分布式延时队列工具类
 * <p>
 *
 * @author Zaki Chen
 * @version 1.0
 * @since 2023-03-08
 **/


@Component
@Slf4j
public class RedisDelayQueueUtil {

    @Resource
    private RedissonClient redissonClient;


    /**
     * 添加延迟队列-按秒计算
     *
     * @param value    队列值
     * @param delay    延迟时间
     * @param queueKey 队列键
     */
    public <T> boolean addBySeconds(@NonNull T value, @NonNull long delay, @NonNull String queueKey) {

        return add(value, delay, TimeUnit.SECONDS, queueKey);
    }


    /**
     * 添加延迟队列-按分钟计算
     *
     * @param value    队列值
     * @param delay    延迟时间
     * @param queueKey 队列键
     */
    public <T> boolean addByMinutes(@NonNull T value, @NonNull long delay, @NonNull String queueKey) {

        return add(value, delay, TimeUnit.MINUTES, queueKey);
    }

    /**
     * 添加延迟队列-按小时计算
     *
     * @param value    队列值
     * @param delay    延迟时间
     * @param queueKey 队列键
     */
    public <T> boolean addByHours(@NonNull T value, @NonNull long delay, @NonNull String queueKey) {

        return add(value, delay, TimeUnit.HOURS, queueKey);
    }


    /**
     * 添加延迟队列-按天计算
     *
     * @param value    队列值
     * @param delay    延迟时间
     * @param queueKey 队列键
     */
    public <T> boolean addByDays(@NonNull T value, @NonNull long delay, @NonNull String queueKey) {

        return add(value, delay, TimeUnit.DAYS, queueKey);
    }


    /**
     * 添加延迟队列-自定义
     *
     * @param value    队列值
     * @param delay    延迟时间
     * @param timeUnit 时间单位
     * @param queueKey 队列键
     */
    public <T> boolean add(@NonNull T value, @NonNull long delay, @NonNull TimeUnit timeUnit, @NonNull String queueKey) {
        if (StrUtil.isBlank(queueKey) || ObjectUtil.isNull(value)) {
            log.error("queueKey({})或value({})数据异常", queueKey, value);
            return false;
        }

        try {

            RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(queueKey);

            RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);

            delayedQueue.offer(value, delay, timeUnit);


            log.info("添加延时队列成功 [QueueKey：{}，Value：{}，DelayTime：{} s] 当前队列长度为{}",
                    queueKey, value, timeUnit.toSeconds(delay), (long) delayedQueue.size());

        } catch (Exception e) {
            log.error("添加延时队列失败,原因为：{}", e.getMessage());
            throw new RuntimeException("添加延时队列失败");
        }
        return true;
    }

    /**
     * 获取延迟队列
     *
     * @param queueKey 队列键
     */
    public Object get(@NonNull String queueKey) throws InterruptedException {
        if (StrUtil.isBlank(queueKey)) {
            log.error("queueKey({})数据异常", queueKey);
        }
        RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(queueKey);

        return blockingDeque.take();
    }

    /**
     * 删除指定队列中的消息
     *
     * @param value    指定删除的消息对象队列值(同队列需保证唯一性)
     * @param queueKey 指定队列键
     */
    public boolean remove(@NonNull Object value, @NonNull String queueKey) {
        if (StrUtil.isBlank(queueKey) || ObjectUtil.isNull(value)) {
            log.error("queueKey({})或value({})数据异常", queueKey, value);
            return false;
        }

        RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(queueKey);

        RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);

        if (delayedQueue.remove(value)) {
            log.info("value({})已从 {} 队列中删除", value, queueKey);
            return true;
        }
        return false;
    }


}
