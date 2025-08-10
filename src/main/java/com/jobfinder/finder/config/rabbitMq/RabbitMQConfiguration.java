package com.jobfinder.finder.config.rabbitMq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@ConditionalOnProperty("${spring.rabbitmq.enabled:false}")
public class RabbitMQConfiguration {

  @Value("${spring.rabbitmq.host:localhost}")
  private static String host;
  @Value("${spring.rabbitmq.port:5672}")
  private static int port;
  @Value("${spring.rabbitmq.username:guest}")
  private static String username;
  @Value("${spring.rabbitmq.password:guest}")
  private static String password;

  @Bean
  public TopicExchange submissionStatusExchange() {
    return new TopicExchange(RabbitMQConstants.SUBMISSION_STATUS_EXCHANGE);
  }

  @Bean
  public Queue submissionStatusChangedQueue() {
    return new Queue(RabbitMQConstants.POST_STATUS_CHANGED_QUEUE, true);
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

}
