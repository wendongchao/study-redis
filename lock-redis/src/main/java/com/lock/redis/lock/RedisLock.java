package com.lock.redis.lock;

import lombok.Data;

/**
 * @author daify
 * @date 2019-08-11
 */
@Data
public class RedisLock {

    /**
     * 锁的key
     */
    private String key;
    /**
     * 锁的值
     */
    private String value;

    public RedisLock(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
