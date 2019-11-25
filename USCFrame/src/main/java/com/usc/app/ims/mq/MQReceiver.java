package com.usc.app.ims.mq;

import com.rabbitmq.client.Channel;
import com.usc.app.ims.config.Endpoint;
import com.usc.app.ims.mq.config.RabbitConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author: lwp
 * @DATE: 2019/10/31 14:38
 * @Description:
 **/
@Component
public class MQReceiver {

    private static MQReceiver mqReceiver;
    @Resource
    private Endpoint endpoint; //引入WebSocket

    @PostConstruct
    public void init() {
        mqReceiver = this;
        mqReceiver.endpoint = endpoint;
    }


    /**
     * content 监听到的消息
     * message 包含 （body：监听到的消息，
     *               MessageProperties[headers,contentType=text/plain contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0
     *               redelivered=false, receivedExchange=交换机, receivedRoutingKey=键值, deliveryTag=1, consumerTag=amq.ctag-h9rec6NPHSwxbbH5IifTEg,
     *               consumerQueue=消息队列]
     * channel 消息通道
     */
    //监听队列
    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void msgReceive(String content, Message message, Channel channel) throws Exception {
        System.err.println("模拟客户端收到的:" + "Content==:" + content + "\n" + "message:" + message + "\n" + "Channel:==" + channel);
        //发送给WebSocket 由WebSocket推送给前端
        mqReceiver.endpoint.sendMessageOnline(content);
        // 确认消息已接收
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
