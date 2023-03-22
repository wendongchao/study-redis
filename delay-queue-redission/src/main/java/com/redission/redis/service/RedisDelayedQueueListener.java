package com.redission.redis.service;


/**
 * <p>
 *  队列事件监听接口，需要实现这个方法
 * <p>
 *
 * @author Zaki Chen
 * @version 1.0
 * @since 2023-03-08
 **/


public interface RedisDelayedQueueListener<T>  {

    /**
     * 超时后，执行该方法
     *
     * @param t
     */
    void execute(T t);
}