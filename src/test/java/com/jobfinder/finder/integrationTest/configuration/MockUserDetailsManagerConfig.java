package com.jobfinder.finder.integrationTest.configuration;

import com.jobfinder.finder.constant.Roles;
import java.util.List;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class MockUserDetailsManagerConfig {
  @Bean
  public UserDetailsService userDetailsService() {
    return new InMemoryUserDetailsManager(List.of(
        // Super Admin
        org.springframework.security.core.userdetails.User.withUsername("superadmin")
            .password("{noop}superadmin") // {noop} indicates no password encoding
            .roles(Roles.SUPER_ADMIN.name())
            .build(),
        // Admin
        org.springframework.security.core.userdetails.User.withUsername("admin")
            .password("{noop}admin") // {noop} indicates no password encoding
            .roles(Roles.ADMIN.name())
            .build(),
        // Applicant
        org.springframework.security.core.userdetails.User.withUsername("applicant")
            .password("{noop}applicant") // {noop} indicates no password encoding
            .roles(Roles.APPLICANT.name())
            .build(),
        // Recruiter
        org.springframework.security.core.userdetails.User.withUsername("recruiter")
            .password("{noop}recruiter") // {noop} indicates no password encoding
            .roles(Roles.RECRUITER.name())
            .build()
    ));
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }
}
