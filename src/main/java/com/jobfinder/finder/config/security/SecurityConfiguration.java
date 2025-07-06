package com.jobfinder.finder.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(
        c ->
            c.requestMatchers("/actuator/**").permitAll() // Allow access to actuator endpoints
             .anyRequest().authenticated() // Require authentication for all other requests
    ).build();
  }

  //todo: use database for user authentication

  //todo: use JWT for stateless authentication

  //todo: password encoding
}
