package com.jdk.delay;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.DelayQueue;

@Service
public class DelayQueueJdk {

	public static void main(String[] args) {
		DelayQueue<MyDelayed> delayQueue = new DelayQueue<MyDelayed>();
		//生产者生产一个5秒的延时任务
		new Thread(new ProducerDelay(delayQueue, 5)).start();
		//开启消费者轮询
		new Thread(new ConsumerDelay(delayQueue)).start();
	}
	
	/**
	 * 延时任务生产者 
	 **/
	public static class ProducerDelay implements Runnable{
		DelayQueue<MyDelayed> delayQueue;
		int delaySecond;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		/**
		 *
		 * @param delayQueue 延迟队列
		 * @param delaySecond  延迟时间
		 * @author dongchao
		 * @return null
		 * @date 2023/3/21 15:37
		 */
		public ProducerDelay(DelayQueue<MyDelayed> delayQueue, int delaySecond){
			this.delayQueue = delayQueue;
			this.delaySecond = delaySecond;
		}

		@Override
		public void run() {
			String orderId = "1010101";
			for (int i = 0; i < 10; i++) {
				//定义一个Delay, 放入到DelayQueue队列中
				MyDelayed delay = new MyDelayed(this.delaySecond, orderId+i);
				delayQueue.add(delay);//向队列中插入一个元素（延时任务）
				System.out.println(sdf.format(new Date())+ " Thread "+Thread.currentThread()+" 添加了一个delay. orderId:"+orderId+i);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	/**
	 * 延时任务消费者
	 **/
	public static class ConsumerDelay implements Runnable{
		
		DelayQueue<MyDelayed> delayQueue;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		public ConsumerDelay(DelayQueue<MyDelayed> delayQueue){
			this.delayQueue = delayQueue;
		}

		@Override
		public void run() {
			//轮询获取DelayQueue队列中当前超时的Delay元素
			while(true){// 一直在循环，消耗资源
				MyDelayed delayed=null;
				try {
					delayed = delayQueue.take();// 获取到期的元素
				} catch (Exception e) {
					e.printStackTrace();
				}
				//如果Delay元素存在,则任务到达超时时间
				if(delayed!=null){
					//处理任务
					System.out.println(sdf.format(new Date())+" Thread "+Thread.currentThread()+" 消费了一个delay. orderId:"+delayed.getOrderId());
				}else{
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("....");
				}
			}
		}
	}
}
