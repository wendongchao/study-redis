package com.simple.redis.common.util;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Collections;
import java.util.Objects;

/**
 * Jedis工具类
 * @auther: dongchao
 * @data: 2023/3/15 16:14
 */
public class JedisUtil {

    private static final Long UNLOCK_SUCCESS = 1L;
    private static JedisPool jedisPool = null;

    static {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        jedisPool = new JedisPool(config, "127.0.0.1", 6379, 6000, "root2014");
    }

    /**
     * 获取jedis
     * @param
     * @author dongchao
     * @return Jedis
     * @date 2023/3/15 16:27
     */
    private static Jedis getJedis() throws JedisException {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (JedisException e) {
            e.printStackTrace();
            throw e;
        }
        return jedis;
    }

    /**
     * 关闭jedis
     * @param jedis
     * @author dongchao
     * @return void
     * @date 2023/3/15 16:27
     */
    protected static void release(Jedis jedis) {
        jedis.close();
    }

    /**
     * 设置锁并设置过期时间
     * @param key 锁
     * @param value 随机值
     * @param expireMillis  过期时间，单位毫秒
     * @author dongchao
     * @return boolean
     * @date 2023/3/15 16:26
     */
    public static boolean setnx(String key, String value, Long expireMillis) {
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = getJedis();
            // nx = not exist, px= 单位是毫秒
            String result = jedis.set(key, value, "NX", "PX", expireMillis);
            if (result != null && result.equalsIgnoreCase("OK")) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(jedis);
        }
        return flag;
    }

    public static boolean unlock(String fullKey, String value) {
        return unlocklua(fullKey, value);
    }

    /**
     * 释放锁,lua
     * @param fullKey 锁
     * @param value  随机值
     * @author dongchao
     * @return boolean
     * @date 2023/3/15 16:28
     */
    private static boolean unlocklua(String fullKey, String value) {
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = getJedis();
            // lua脚本，不了解的话，先了解
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            // 成功返回1，失败返回0
            Object result = jedis.eval(script, Collections.singletonList(fullKey), Collections.singletonList(value));
            flag = Objects.equals(UNLOCK_SUCCESS, result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(jedis);
        }
        return flag;
    }

    /**
     * 释放锁，watch
     * @param fullKey 锁
     * @param value  随机值
     * @author dongchao
     * @return boolean
     * @date 2023/3/15 16:34
     */
    private static boolean unlockwatch(String fullKey, String value) {
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = getJedis();
            jedis.watch(fullKey);
            String existValue = jedis.get(fullKey);
            if (Objects.equals(value, existValue)) {
                jedis.del(fullKey);
                flag = true;
            } else {
                System.out.println("unlock failed ; key:" + fullKey + ",value:" + value + ",existValue:" + existValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.unwatch();
            release(jedis);
        }
        return flag;
    }


}
