package com.base.redis.pubsub.controller;

import com.base.redis.pubsub.service.PubSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author daify
 * @date 2019-07-25 13:46
 **/
@RestController
@RequestMapping("pub")
public class PubController {
    
    @Autowired
    private PubSend send;

    /**
     * 测试消息发送
     * @param message
     * @return
     */
    @RequestMapping(value = "send/{message}",method = RequestMethod.GET)
    public String send(@PathVariable("message") String message) {
        send.sendTopicMessage(message);
        return message;
    }
}
