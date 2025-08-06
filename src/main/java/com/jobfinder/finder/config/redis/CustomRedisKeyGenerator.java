package com.jobfinder.finder.config.redis;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.springframework.cache.interceptor.KeyGenerator;

public class CustomRedisKeyGenerator implements KeyGenerator {
  @Override
  public Object generate(Object target, Method method, Object... params) {
    List<String> paramList = Arrays.stream(params).filter(Objects::nonNull).map(Object::toString).toList();
    return String.format("%s_%s_%s",
        target.getClass().getSimpleName(),
        method.getName(),
        String.join(":", paramList));
  }
}
