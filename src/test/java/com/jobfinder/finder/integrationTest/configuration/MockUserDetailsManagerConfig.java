package com.jobfinder.finder.integrationTest.configuration;

import java.util.List;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class MockUserDetailsManagerConfig {
/*
  @Bean
  public UserDetailsService userDetailsService() {
    return new InMemoryUserDetailsManager(
        List.of(
            User.builder()
                .username("applicant")
                .password("{noop}applicant") // {noop} indicates no password encoding
                .roles("APPLICANT")
                .build(),
            User.builder()
                .username("recruiter")
                .password("{noop}recruiter") // {noop} indicates no password encoding
                .roles("RECRUITER")
                .build(),
            User.builder()
                .username("admin")
                .password("{noop}admin") // {noop} indicates no password encoding
                .roles("ADMIN")
                .build(),
            User.builder()
                .username("superadmin")
                .password("{noop}superadmin") // {noop} indicates no password encoding
                .roles("SUPER_ADMIN")
                .build()
        )
    );
  }


 */

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
