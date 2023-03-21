package test;

import com.job.redis.delay.bean.Job;
import com.job.redis.delay.container.JobPool;
import com.job.redis.delay.service.JobService;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author daify
 * @date 2019-07-29 9:24
 **/
@Slf4j
public class JobServiceTest extends TestCase {

    @Autowired
    private JobService jobService;
    @Autowired
    private JobPool jobPool;

    private Job getProcessJob() {
        Job job = new Job();
        job.setId(1L);
        job.setMessage("非延时任务");
        job.setTopic("任务");
        return job;
    }

    private Job getDelayJob() {
        Job job = new Job();
        job.setId(1L);
        job.setMessage("延时任务");
        job.setTopic("任务");
        return job;
    }

    @Before
    public void initQueue() {
        /*jobService.removeAll("任务",1L);
        jobService.removeAll("任务",2L);*/
    }

    @Test
    public void addJob() {
        /*Job job = jobService.addJob(getProcessJob(), null);
        Job job1 = jobPool.getJob(job.getId());
        log.info("数据:{}", JSON.toJSONString(job1));
        Assert.assertNotNull(job1);*/
    }

    @Test
    public void deleteJob() {
        /*Job job = jobService.addJob(getProcessJob(), null);
        Job job1 = jobPool.getJob(job.getId());
        log.info("数据:{}", JSON.toJSONString(job1));
        jobService.deleteJob(job.getId());
        Job job3 = jobPool.getJob(job.getId());
        Assert.assertEquals(job3.getStatus(), JobStatus.DELETED);*/
    }

    @Test
    public void getProcess() {
        //Job job = jobService.addJob(getDelayJob(), 1000L);
        //
        //Job job1 = jobPool.getJob(job.getId());
        //log.info("数据:{}", JSON.toJSONString(job1));
        //
        //try {
        //    Thread.sleep(2000L);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
        //Job process = jobService.getProcess(job.getTopic());
        //log.info("数据process:{}", JSON.toJSONString(process));
        //Assert.assertNotNull(process);
    }

    @Test
    public void finishProcess() {
        /*Job job = jobService.addJob(getDelayJob());

        Job job1 = jobPool.getJob(job.getId());
        log.info("数据:{}", JSON.toJSONString(job1));

        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Job process = jobService.getProcessJob(job.getTopic());
        log.info("数据process:{}", JSON.toJSONString(process));
        Assert.assertNotNull(process);
        
        jobService.finishJob(process.getId());
        
        Job job3 = jobPool.getJob(process.getId());
        Assert.assertNull(job3);*/
    }
}
