package com.jobfinder.finder.integrationTest.configuration;

import com.jobfinder.finder.constant.Roles;
import java.util.List;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@Profile("test")
public class MockUserDetailsManagerConfig {
  @Bean
  public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable) // will use stateless authentication, so CSRF protection is not needed
        .userDetailsService(testUserDetailsService())
        .authorizeHttpRequests(
            c ->
                // Define access rules for different endpoints
                //-- Public endpoints
                c.requestMatchers("/user/login", "/user/register").permitAll() // Allow public access to login and register
                    //-- Admin related endpoints
                    .requestMatchers("/actuator/**").hasAnyRole(Roles.SUPER_ADMIN.name(), Roles.ADMIN.name()) // Allow access to actuator endpoints to admins
                    .requestMatchers(HttpMethod.DELETE, "/admin/**").hasRole(Roles.SUPER_ADMIN.name())
                    .requestMatchers("/admin/**").hasAnyRole(Roles.SUPER_ADMIN.name(), Roles.ADMIN.name())
                    //-- User related endpoints
                    .requestMatchers(HttpMethod.DELETE, "/user/**").hasAnyRole(Roles.SUPER_ADMIN.name(), Roles.ADMIN.name(), Roles.APPLICANT.name())
                    .requestMatchers("/user/**").hasAnyRole(Roles.APPLICANT.name())
                    //-- Post related endpoints
                    .requestMatchers(HttpMethod.DELETE, "/post/**")
                    .hasAnyRole(Roles.RECRUITER.name()
                        , Roles.ADMIN.name()
                        , Roles.SUPER_ADMIN.name()) // admins and recruiters can delete posts
                    .requestMatchers(HttpMethod.GET, "/post/**").hasAnyRole(Roles.APPLICANT.name())
                    .requestMatchers(HttpMethod.POST, "/post/**").hasAnyRole(Roles.RECRUITER.name())
                    .requestMatchers(HttpMethod.PATCH, "/post/**").hasAnyRole(Roles.RECRUITER.name())
                    .requestMatchers(HttpMethod.DELETE, "/post/**").hasAnyRole(Roles.RECRUITER.name(), Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())
                    //-- Submission related endpoints
                    .requestMatchers(HttpMethod.POST, "/submit").hasRole(Roles.APPLICANT.name())
                    .requestMatchers(HttpMethod.GET, "/submit").hasAnyRole(Roles.APPLICANT.name(), Roles.RECRUITER.name())
                    .requestMatchers(HttpMethod.PATCH, "/submit/**").hasRole(Roles.RECRUITER.name())
                    .anyRequest().authenticated() // Require authentication for all other requests
        )
        .httpBasic(Customizer.withDefaults())
        .build();
  }

  @Bean
  @Primary
  public UserDetailsService testUserDetailsService() {
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
  public PasswordEncoder noOpPasswordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }
}
