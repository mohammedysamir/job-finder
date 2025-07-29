package com.jobfinder.finder.config.redis;

import java.lang.reflect.Method;
import org.springframework.cache.interceptor.KeyGenerator;

import static org.hibernate.internal.util.collections.ArrayHelper.toStringArray;

public class CustomRedisKeyGenerator implements KeyGenerator {
  @Override
  public Object generate(Object target, Method method, Object... params) {
    return String.format("%s_%s_%s",
        target.getClass().getSimpleName(),
        method.getName(),
        String.join(":", toStringArray(params)));
  }
}
