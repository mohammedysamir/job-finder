package com.jobfinder.finder.integrationTest.configuration;

import com.jobfinder.finder.config.security.SecurityConfiguration;
import com.jobfinder.finder.constant.Roles;
import java.util.List;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@Import(SecurityConfiguration.class)
public class MockUserDetailsManagerConfig {

  @Bean
  public UserDetailsManager userDetailsManager() {
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

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
