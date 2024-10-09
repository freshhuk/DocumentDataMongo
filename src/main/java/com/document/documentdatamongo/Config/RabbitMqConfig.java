package com.document.documentdatamongo.Config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${queue.name}")
    private String queueName;

    @Value("${spring.rabbitmq.username}")
    private String queueUserName;

    @Value("${spring.rabbitmq.password}")
    private String queuePassword;

    @Bean
    public Queue queue(){
        return new Queue(queueName, false);
    }

    @Bean
    public CachingConnectionFactory connectionFactory(){
        CachingConnectionFactory connection = new CachingConnectionFactory("localhost");
        connection.setUsername(queueUserName);
        connection.setPassword(queuePassword);
        return connection;
    }
    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter());
        return template;
    }
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
