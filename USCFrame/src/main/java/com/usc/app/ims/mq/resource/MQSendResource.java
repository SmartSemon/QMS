package com.usc.app.ims.mq.resource;

import com.usc.app.ims.mq.service.MQSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lwp
 * @DATE: 2019/10/31 14:37
 * @Description:
 **/
@RestController
public class MQSendResource {

    @Autowired
    private MQSenderService mqSenderService;

    @GetMapping("/chat")
    public void send() {
        mqSenderService.send();
    }

}
