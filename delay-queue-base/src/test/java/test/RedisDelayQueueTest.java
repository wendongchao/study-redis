package test;

import com.queue.redis.RedisDelayQueue;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @auther: dongchao
 * @data: 2023/3/21 18:01
 */
@Slf4j
public class RedisDelayQueueTest extends TestCase {

    @Resource
    private RedisDelayQueue redisDelayQueue;

    public void produce() {
        for (int i = 0; i < 30; i++) {
            redisDelayQueue.produce("topic"+i%3 , "hello message"+i , new Date(System.currentTimeMillis()+i*1000));
        }
    }


    public void consumer() throws InterruptedException {
        redisDelayQueue.consumer("topic0", (msg)->{
            log.info("topic【{}】收到消息：{}","topic0",msg);
            return true;
        });
        redisDelayQueue.consumer("topic1", (msg)->{
            log.info("topic【{}】收到消息：{}","topic1",msg);
            return true;
        });
        redisDelayQueue.consumer("topic2", (msg) -> {
            log.info("topic【{}】收到消息：{}", "topic2", msg);
            return true;
        });

        TimeUnit.MINUTES.sleep(10);
    }
}
