package com.jobfinder.finder.config.security;

import com.jobfinder.finder.constant.Roles;
import com.jobfinder.finder.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@Profile("!test") // Exclude this configuration in tests
public class SecurityConfiguration {
  private final UserRepository userRepository;
  private final JobFinderUserDetailsService userDetailsService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable) // will use stateless authentication, so CSRF protection is not needed
        .userDetailsService(userDetailsService)
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
//todo: use JWT for stateless authentication

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
/*
Roles:
1. SuperAdmin - CRUD operations on Admins and delete posts.
2. Admin - delete posts.
3. Applicant - apply for jobs, view posts, login, logout, register, enable email notification for skills/positions.
4. Recruiter - CRUD operations on created posts, view applicants, login, logout, register, view posts by the same recruiter.
 */