package com.usc.app.ims.mq.service.impl;

import com.usc.app.ims.mq.config.RabbitConfig;
import com.usc.app.ims.mq.service.MQSenderService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: lwp
 * @DATE: 2019/11/1 14:49
 * @Description:
 **/
@Service("MQSenderService")
public class MQSendServiceImpl implements MQSenderService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * Topic交换机模式（发送消息）
     */
    @Override
    public void send() {
        long receiveUserId = 456;
        String message = "rabbitmq哈喽";
        amqpTemplate.convertAndSend(RabbitConfig.EXCHANGE, "chat" + receiveUserId, message);
        System.err.println("发送成功");
    }

}
