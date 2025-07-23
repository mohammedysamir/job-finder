package com.jobfinder.finder.integrationTest.configuration;

import com.jobfinder.finder.constant.Roles;
import java.util.List;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
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
public class MockUserDetailsManagerConfig {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .httpBasic(Customizer.withDefaults())
        .csrf(customizer -> customizer.disable()) // will use stateless authentication, so CSRF protection is not needed
        .authorizeHttpRequests(
            c ->
                // Define access rules for different endpoints
                //-- Public endpoints
                c.requestMatchers("/login", "/register").permitAll() // Allow public access to login and register
                    //-- Admin related endpoints
                    .requestMatchers("/actuator/**").hasAnyRole(Roles.SUPER_ADMIN.name(), Roles.ADMIN.name()) // Allow access to actuator endpoints to admins
                    .requestMatchers("/admin/**").hasRole(Roles.SUPER_ADMIN.name())
                    //-- User related endpoints
                    .requestMatchers("/user/**").hasAnyRole(Roles.APPLICANT.name())
                    //-- Post related endpoints
                    .requestMatchers(HttpMethod.DELETE, "/post/**")
                    .hasAnyRole(Roles.RECRUITER.name()
                        , Roles.ADMIN.name()
                        , Roles.SUPER_ADMIN.name()) // admins and recruiters can delete posts
                    .requestMatchers(HttpMethod.GET, "/post/**").hasAnyRole(Roles.APPLICANT.name())
                    .requestMatchers(HttpMethod.POST, "/post/**").hasAnyRole(Roles.RECRUITER.name())
                    .requestMatchers(HttpMethod.PATCH, "/post/**").hasAnyRole(Roles.RECRUITER.name())
                    .anyRequest().authenticated() // Require authentication for all other requests
        ).build();
  }

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
