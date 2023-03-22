package com.redission.redis.service.impl;

import com.redission.redis.entity.Task;
import com.redission.redis.service.RedisDelayedQueueListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  处理超时任务
 * <p>
 *
 * @author Zaki Chen
 * @version 1.0
 * @since 2023-03-08
 **/
@Component
@Slf4j
public class TaskTimeoutServiceImpl implements RedisDelayedQueueListener<Task> {

    @Override
    public void execute(Task s) {
        log.info("任务超时 [{}]", s);
        // TODO 订单支付超时，自动取消订单处理业务...
    }
}
