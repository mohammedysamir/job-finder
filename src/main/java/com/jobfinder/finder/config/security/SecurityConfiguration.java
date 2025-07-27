package com.jobfinder.finder.config.security;

import com.jobfinder.finder.constant.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

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
                    .requestMatchers(HttpMethod.DELETE,"/admin/**").hasRole(Roles.SUPER_ADMIN.name())
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
                    .anyRequest().authenticated() // Require authentication for all other requests
        ).build();
  }

  //todo: use database for user authentication

  //todo: use JWT for stateless authentication

  //todo: password encoding
}
/*
Roles:
1. SuperAdmin - CRUD operations on Admins and delete posts.
2. Admin - delete posts.
3. Applicant - apply for jobs, view posts, login, logout, register, enable email notification for skills/positions.
4. Recruiter - CRUD operations on created posts, view applicants, login, logout, register, view posts by the same recruiter.
 */