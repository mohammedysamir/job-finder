package com.jobfinder.finder.config.security;

import com.jobfinder.finder.constant.UserStatus;
import com.jobfinder.finder.entity.UserEntity;
import com.jobfinder.finder.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class JobFinderUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    boolean isEnabled = (user.getUserStatus() == UserStatus.VERIFIED || user.getUserStatus() == UserStatus.UNVERIFIED);
    return new JobFinderUserDetails(user.getEmail(), user.getUsername(),
        user.getPassword(), user.getRole(), user.getUserStatus(), isEnabled);
  }
}
