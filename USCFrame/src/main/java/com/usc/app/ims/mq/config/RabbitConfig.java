package com.usc.app.ims.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lwp
 * @DATE: 2019/10/31 15:44
 * @Description:
 **/
@Configuration
public class RabbitConfig {

    /**
     * 聊天队列，交换机，路由key
    */
    public static final String QUEUE = "notice";
    public static final String EXCHANGE = "usc";
    public static final String KEY = "chat";

    /**
     * 聊天队列
     */
    @Bean
    public Queue chatQueue(){
        return new Queue(QUEUE, true);
    }

    /**
     * 聊天交换机
     */
    @Bean
    public TopicExchange chatExchange(){
        return new TopicExchange(EXCHANGE);
    }

    /**
     * 聊天交换机，聊天队列与路由健绑定
     */
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(chatQueue()).to(chatExchange()).with(KEY);
    }

}
