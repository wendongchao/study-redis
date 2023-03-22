package com.redission.redis.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * <p>
 *  延迟队列业务枚举
 * <p>
 *
 * @author Zaki Chen
 * @version 1.0
 * @since 2023-03-08
 **/

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum RedisDelayQueueEnum {


    /**
     * BeanId 首字母要小写！！！！！BeanId 首字母要小写！！！！！BeanId 首字母要小写！！！！！
     */
    TASK_TIMEOUT("TASK_TIMEOUT", "taskTimeoutServiceImpl");


    /**
     * 延迟队列的队列键
     */
    private String code;


    /**
     * 该延迟队列具体业务实现的 Bean
     * 可通过 Spring 的上下文获取
     */
    private String beanId;

}