package com.example.myprojectbackend.config;

import ch.qos.logback.classic.pattern.MessageConverter;
import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitConfiguration {
    @Bean("mailQueue")
    public Queue mailQueue(){
        return QueueBuilder
                .durable("mail")
                .build();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new MessageConverter();
    }
}
