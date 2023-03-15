package com.simple.redis.test;

import com.simple.redis.common.LockNumAdd;
import com.simple.redis.common.NoLockNumAdd;
import junit.framework.TestCase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @auther: dongchao
 * @data: 2023/3/15 17:32
 */
public class TestRun extends TestCase {

    // 测试加锁与不加锁的区别
    public void test01() throws InterruptedException {
        int notLockAdd = testNotLockAdd();
        int lockAdd = testLockAdd();
        System.out.println("notLockAdd = " + notLockAdd);
        System.out.println("lockAdd = " + lockAdd);
    }

    /**
     * 无锁加
     * @param
     * @author dongchao
     * @return int
     * @date 2023/3/15 17:43
     */
    private static int testNotLockAdd() throws InterruptedException {
        int threadCount = 10;
        NoLockNumAdd add = new NoLockNumAdd(threadCount);
        ExecutorService exec = Executors.newFixedThreadPool(threadCount);// 创建10个线程的线程池
        for (int i = 0; i < threadCount; i++) {
            exec.submit(add);
        }
        add.end.await();
        int result = add.getNum();
        exec.shutdown();
        return result;
    }

    /**
     * 有锁加
     * @param
     * @author dongchao
     * @return int
     * @date 2023/3/15 17:43
     */
    private static int testLockAdd() throws InterruptedException {
        int threadCount = 10;
        LockNumAdd add = new LockNumAdd(threadCount);
        ExecutorService exec = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            exec.submit(add);
        }
        add.end.await();
        int result = add.getNum();
        exec.shutdown();
        return result;
    }
}
