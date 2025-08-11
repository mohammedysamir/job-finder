package com.jobfinder.finder.integrationTest.service;

import com.jobfinder.finder.config.redis.RedisConfiguration;
import com.jobfinder.finder.integrationTest.configuration.MockUserDetailsManagerConfig;
import com.jobfinder.finder.integrationTest.configuration.JobFinderTestConfiguration;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

@Import({JobFinderTestConfiguration.class})
@EnableCaching
public abstract class CacheTestIntializer {

  @Autowired
  CacheManager cacheManager;

  @BeforeEach
  public void clearCache() {
    Objects.requireNonNull(cacheManager.getCache(RedisConfiguration.CACHE_NAME)).clear();
  }
}
