package com.simple.redis.common.util;

/**
 *  基于redis setnx的 分布式锁 实现, 前提是所有的锁都要有锁定时间.
 *  获取锁的时候,需要指定value,在unlock的时候,会根据value判断是否remove
 * @auther: dongchao
 * @data: 2023/3/15 16:38
 */
public class LockUtil {
    private static final String LOCK_PREFIX = "LOCK";// 锁前缀
    private static final Integer DEFAULT_LOCK_TIME = 20;// 默认锁定时间毫秒秒
    private static final Long DEFAULT_SLEEP_TIME = 100L;// 默认sleep时间,100毫秒

    /**
     * 获取缓存的value,随机值,使不同的锁value不同 (多服务器可以使用redis时间+客户端标识等)
     * @param
     * @author dongchao
     * @return String
     * @date 2023/3/15 16:50
     */
    public static String getLockValue() {
        int random = (int) ((Math.random() * 9 + 1) * 100000);
        long now = System.currentTimeMillis();
        return String.valueOf(now) + String.valueOf(random);
    }

    public static void lock(String key, String value) {
        lock(key, value, DEFAULT_LOCK_TIME);
    }

    public static void lock(String key, String value, int lockTime) {
        lock(key, value, lockTime, true);
    }

    /**
     * 锁定key
     * @param key
     * @param value
     * @param lockTime 锁过期时长
     * @param reTry  是否重试
     * @author dongchao
     * @return boolean
     * @date 2023/3/15 17:00
     */
    private static boolean lock(String key, String value, int lockTime, boolean reTry) {
        return lock(key, value, lockTime, reTry, 0, false, 0);
    }

    public static boolean tryLock(String key, String value) {
        return tryLock(key, value, DEFAULT_LOCK_TIME);
    }

    public static boolean tryLock(String key, String value, int lockTime) {
        return lock(key, value, lockTime, false);
    }

    public static boolean tryLock(String key, String value, int lockTime, long timeOutMillis) {
        return lock(key, value, lockTime, true, 0, true, timeOutMillis);
    }

    /**
     * 释放锁
     * @param key 锁
     * @param value 随机值
     * @author dongchao
     * @return boolean
     * @date 2023/3/15 16:58
     */
    public static boolean unlock(String key, String value) {
        String fullKey = getFullKey(key);
        boolean success = JedisUtil.unlock(fullKey, value);
        if (success) {
            System.out.println("unlock success ; key:" + key + ",value:" + value);
        } else {
            System.out.println("unlock failed ; key:" + key + ",value:" + value);
        }
        return success;
    }

    /**
     * 锁定key
     * @param key
     * @param value 随机值
     * @param lockTime 锁超时时间
     * @param reTry 失败是否重试
     * @param reTryNum 重试次数
     * @param needTimeOut 是否需要判断超时时间
     * @param timeOutMillis 尝试超时时间(秒)
     * @author dongchao
     * @return boolean
     * @date 2023/3/15 17:03
     */
    private static boolean lock(String key, String value, int lockTime, boolean reTry, int reTryNum,
                                boolean needTimeOut, long timeOutMillis) {
        String paramlog = "%s,lock come in ; key:%s,value:%s,lockTime:%s,reTry:%s,reTryNum:%s,needTimeOut:%s,timeOutMillis:%s";
        System.out.println(String.format(paramlog,Thread.currentThread().getName(),key,value,lockTime,reTry,reTryNum,needTimeOut,timeOutMillis));
        reTryNum++;
        String fullKey = getFullKey(key);

        // setnx 并设置超时时间
        boolean success = JedisUtil.setnx(fullKey, value, (long) lockTime * 1000);
        // 获取成功,直接返回
        if (success) {
            String successlog = "lock success ; key:%s,value:%s,lockTime:%s,reTry:%s,reTryNum:%s,needTimeOut:%s,timeOutMillis:%s";
            System.out.println(String.format(successlog,key,value,lockTime,reTry,reTryNum,needTimeOut,timeOutMillis));
            return true;
        }

        // 获取失败,不需要重试,直接返回
        if (!reTry) {
            String failelog = "lock failed ; key:%s,value:%s,lockTime:%s,reTry:%s,reTryNum:%s,needTimeOut:%s,timeOutMillis:%s";
            System.out.println(String.format(failelog,key,value,lockTime,reTry,reTryNum,needTimeOut,timeOutMillis));
            return false;
        }

        // 获取失败, 且已超时,返回
        if (needTimeOut && timeOutMillis <= 0) {
            String failelog = "lock failed ; key:%s,value:%s,lockTime:%s,reTry:%s,reTryNum:%s,needTimeOut:%s,timeOutMillis:%s";
            System.out.println(String.format(failelog,key,value,lockTime,reTry,reTryNum,needTimeOut,timeOutMillis));
            return false;
        }

        // 获取sleep时间
        long sleepMillis = getSleepMillis(needTimeOut, timeOutMillis);

        // sleep后重新获取锁
        sleep(sleepMillis);

        // 大于100次,打印warning日志
        if (reTryNum > 100) {
            String warninglog = "lock warning ; key:%s,value:%s,lockTime:%s,reTry:%s,reTryNum:%s,needTimeOut:%s,timeOutMillis:%s";
            System.out.println(String.format(warninglog,key,value,lockTime,reTry,reTryNum,needTimeOut,timeOutMillis));
        }

        return lock(key, value, lockTime, reTry, reTryNum, needTimeOut, timeOutMillis);
    }

    /**
     * 获取休眠时间
     * @param needTimeOut 是否需要判断超时时间
     * @param timeOutMillis 尝试超时时间，单位毫秒
     * @author dongchao
     * @return long
     * @date 2023/3/15 16:54
     */
    private static long getSleepMillis(boolean needTimeOut, long timeOutMillis) {
        long sleepMillis = DEFAULT_SLEEP_TIME;
        if (needTimeOut) {
            timeOutMillis = timeOutMillis - DEFAULT_SLEEP_TIME;
            if (timeOutMillis < DEFAULT_SLEEP_TIME && timeOutMillis > 0) {
                sleepMillis = timeOutMillis;
            }
        }
        return sleepMillis;
    }

    /**
     * 休眠时长
     * @param sleepMillis  时间，单位毫秒
     * @author dongchao
     * @return void
     * @date 2023/3/15 16:53
     */
    private static void sleep(long sleepMillis) {
        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取锁字符串
     * @param key  锁
     * @author dongchao
     * @return String
     * @date 2023/3/15 16:53
     */
    private static String getFullKey(String key) {
        return LOCK_PREFIX + ":" + key;
    }

}
