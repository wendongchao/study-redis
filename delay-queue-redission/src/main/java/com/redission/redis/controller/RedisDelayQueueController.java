package com.redission.redis.controller;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.redission.redis.entity.Task;
import com.redission.redis.utils.RedisDelayQueueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.redission.redis.utils.RedisDelayQueueEnum.TASK_TIMEOUT;


/**
 * <p>
 *  延迟队列简单示例
 * <p>
 *
 * @author Zaki Chen
 * @version 1.0
 * @since 2023-03-08
 **/

@RestController
@Slf4j
public class RedisDelayQueueController {

    @Resource
    private RedisDelayQueueUtil redisDelayQueueUtil;


    @GetMapping("/addQueue")
    public String addQueue() {
        Task task = new Task(RandomUtil.randomNumbers(6), "study Redis");

        redisDelayQueueUtil.addBySeconds(task, 30, TASK_TIMEOUT.getCode());

        return JSONUtil.toJsonStr(task);
    }

    @PostMapping("/deleteQueue")
    public String deleteQueue(@RequestBody Task task) {
        boolean b = redisDelayQueueUtil.remove(task, TASK_TIMEOUT.getCode());

        if(b){
            return "Success";
        }else {
            return "False";
        }

    }


}