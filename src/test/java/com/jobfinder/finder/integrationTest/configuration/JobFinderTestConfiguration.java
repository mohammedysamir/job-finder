package com.jobfinder.finder.integrationTest.configuration;

import com.jobfinder.finder.config.rabbitMq.RabbitMQConfiguration;
import com.jobfinder.finder.config.redis.CustomRedisKeyGenerator;
import com.jobfinder.finder.config.redis.RedisConfiguration;
import com.jobfinder.finder.config.security.SecurityConfiguration;
import java.util.List;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.mock;

@Import({RedisConfiguration.class, MockUserDetailsManagerConfig.class})
@EnableAutoConfiguration(exclude = { RabbitAutoConfiguration.class})
@Configuration
public class JobFinderTestConfiguration {
  //--Redis cache configuration for testing--//
  @Bean
  public CacheManager cacheManager() {
    // Use a simple in-memory cache manager for testing purposes
    SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
    simpleCacheManager.setCaches(List.of(new ConcurrentMapCache(RedisConfiguration.CACHE_NAME, false)));
    return simpleCacheManager;
  }

  @Bean
  public KeyGenerator customRedisKeyGenerator() {
    return new CustomRedisKeyGenerator();
  }

  //-- Email service mock for testing --//
  @Bean
  public JavaMailSender javaMailSender() {
    return mock(JavaMailSender.class);
  }

  //-- RabbitMQ configuration mock for testing --//
  @Bean
  public RabbitMQConfiguration rabbitMQConfiguration() {
    return mock(RabbitMQConfiguration.class);
  }
  @Bean
  public RabbitTemplate rabbitTemplate() {
    return mock(RabbitTemplate.class);
  }
}
