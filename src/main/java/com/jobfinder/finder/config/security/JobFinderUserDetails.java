package com.jobfinder.finder.config.security;

import com.jobfinder.finder.constant.Roles;
import com.jobfinder.finder.constant.UserStatus;
import com.jobfinder.finder.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties.Simple;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public class JobFinderUserDetails implements UserDetails {
  private final String email;
  private final String username;
  private final String password;
  private final Roles role;
  private final UserStatus userStatus;
  private final boolean isEnabled;

  public JobFinderUserDetails(String email, String username, String password, Roles role, UserStatus userStatus, boolean isEnabled) {
    this.email = email;
    this.username = username;
    this.password = password;
    this.role = role;
    this.userStatus = userStatus;
    this.isEnabled = isEnabled;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public boolean isEnabled() {
    return this.userStatus == UserStatus.VERIFIED || this.userStatus == UserStatus.UNVERIFIED;
  }

  public String getEmail() {
    return email;
  }

  public Roles getRole() {
    return role;
  }

  public UserStatus getUserStatus() {
    return userStatus;
  }

  @Override
  public String getUsername() {
    return this.username;
  }
}
