package com.simple.redis.test;

import com.lock.redis.lock.RedisLock;
import com.lock.redis.lock.RedisLockUtils;
import com.lock.redis.lock.RedisTemplateLockUtils;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * @author daify
 * @date 2019-08-11
 */
@Slf4j
public class RedisLockTest extends TestCase {

    @Autowired
    private RedisLockUtils manager;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisTemplateLockUtils redisTemplateLockUtils;

    @Test
    public void testTemplateLock() {
        redisTemplate.delete("redisLock");
        redisTemplate.delete("redisLock2");

        RedisLock redisLock1 = redisTemplateLockUtils.getLock("redisLock", 10000L, 5000L);
        if (redisLock1 == null) {
            log.info("未获得锁");
        } else {
            log.info("获得锁");
        }
        Assert.assertNotNull(redisLock1);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 因为非重入锁，所以此时再获取锁失败
        RedisLock redisLock2 = redisTemplateLockUtils.getLock("redisLock", 10000L, 5000L);
        Assert.assertNull(redisLock2);
        if (redisLock2 == null) {
            log.info("未获得锁");
        } else {
            log.info("获得锁");
        }
    }


    @Test
    public void testLock() {
        redisTemplate.delete("redisLock");
        redisTemplate.delete("redisLock2");

        RedisLock redisLock1 = manager.getLock("redisLock", 10000L, 5000L);
        if (redisLock1 == null) {
            log.info("未获得锁");
        } else {
            log.info("获得锁");
        }
        Assert.assertNotNull(redisLock1);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 因为非重入锁，所以此时再获取锁失败
        RedisLock redisLock2 = manager.getLock("redisLock", 10000L, 5000L);
        Assert.assertNull(redisLock2);
        if (redisLock2 == null) {
            log.info("未获得锁");
        } else {
            log.info("获得锁");
        }
    }

    @Test
    public void testLock2() {

        redisTemplate.delete("redisLock");
        redisTemplate.delete("redisLock2");

        RedisLock redisLock3 = manager.getLock("redisLock2", 10000L, 5000L);
        if (redisLock3 == null) {
            log.info("未获得锁");
        } else {
            log.info("获得锁");
        }
        Assert.assertNotNull(redisLock3);
        try {
            Thread.sleep(15000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 因为非重入锁，所以此时再获取锁失败
        RedisLock redisLock4 = manager.getLock("redisLock2", 10000L, 5000L);
        Assert.assertNotNull(redisLock4);
        if (redisLock4 == null) {
            log.info("未获得锁");
        } else {
            log.info("获得锁");
        }
    }

    @Test
    public void testLock3() {

        redisTemplate.delete("redisLock");
        redisTemplate.delete("redisLock2");
        redisTemplate.delete("redisLock3");

        RedisLock redisLock3 = manager.getLock("redisLock3", 10000L, 5000L);
        if (redisLock3 == null) {
            log.info("未获得锁");
        } else {
            log.info("获得锁");
        }
        Assert.assertNotNull(redisLock3);
        manager.releaseLock(redisLock3);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 因为非重入锁，所以此时再获取锁失败
        RedisLock redisLock4 = manager.getLock("redisLock2", 10000L, 5000L);
        Assert.assertNotNull(redisLock4);
        if (redisLock4 == null) {
            log.info("未获得锁");
        } else {
            log.info("获得锁");
        }
    }


}
