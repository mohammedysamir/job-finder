package com.jobfinder.finder.config.rabbitMq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true")
@Configuration
public class RabbitMQConfiguration {

  @Value("${spring.rabbitmq.host:localhost}")
  private String host;
  @Value("${spring.rabbitmq.port:5672}")
  private int port;
  @Value("${spring.rabbitmq.username:guest}")
  private String username;
  @Value("${spring.rabbitmq.password:guest}")
  private String password;

  @Bean
  public TopicExchange submissionStatusExchange() {
    return new TopicExchange(RabbitMQConstants.SUBMISSION_STATUS_EXCHANGE);
  }

  @Bean
  public Queue submissionStatusChangedQueue() {
    return new Queue(RabbitMQConstants.SUBMISSION_STATUS_CHANGED_QUEUE, true);
  }

  @Bean
  public Binding submissionStatusChangedBinding(Queue submissionStatusChangedQueue, TopicExchange submissionStatusExchange) {
    return BindingBuilder.bind(submissionStatusChangedQueue)
        .to(submissionStatusExchange)
        .with(RabbitMQConstants.SUBMISSION_STATUS_CHANGED_ROUTING_KEY);
  }

  @Bean
  public TopicExchange postStatusExchange() {
    return new TopicExchange(RabbitMQConstants.POST_STATUS_EXCHANGE);
  }

  @Bean
  public Queue postStatusChangedQueue() {
    return new Queue(RabbitMQConstants.POST_STATUS_CHANGED_QUEUE, true);
  }

  @Bean
  public Binding postStatusChangedBinding(Queue postStatusChangedQueue, TopicExchange postStatusExchange) {
    return BindingBuilder.bind(postStatusChangedQueue)
        .to(postStatusExchange)
        .with(RabbitMQConstants.POST_STATUS_CHANGED_ROUTING_KEY);
  }

  //-- Beans for User Registration and Login Notifications --//
  @Bean
  public TopicExchange userStatusExchange() {
    return new TopicExchange(RabbitMQConstants.USER_STATUS_EXCHANGE);
  }

  @Bean
  public Queue userStatusChangedQueue() {
    return new Queue(RabbitMQConstants.USER_STATUS_CHANGED_QUEUE, true);
  }

  @Bean
  public Binding userStatusChangedBinding(Queue userStatusChangedQueue, TopicExchange userStatusExchange) {
    return BindingBuilder.bind(userStatusChangedQueue)
        .to(userStatusExchange)
        .with(RabbitMQConstants.USER_STATUS_CHANGED_ROUTING_KEY);
  }


}
