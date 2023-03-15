package com.simple.redis.common;

public abstract class NumAdd implements Runnable {
	private int num;

	public int getNum() {
		return num;
	}

	public void addNum() {
		// 稍微停顿,模拟业务
		sleep();
		num++;
	}

	// 重置
	public void resetNum() {
		num = 0;
	}

	private void sleep() {
		try {
			Thread.sleep(1);// 休眠1毫秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
