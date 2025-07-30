package com.jobfinder.finder.integrationTest.configuration;

import com.jobfinder.finder.config.redis.CustomRedisKeyGenerator;
import com.jobfinder.finder.config.redis.RedisConfiguration;
import java.util.List;
import org.apache.el.util.ConcurrentCache;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(RedisConfiguration.class)
@TestConfiguration
public class CacheTestConfiguration {
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
}
