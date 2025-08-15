package com.jobfinder.finder.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VerificationTokenMessage {
  String token;
  String email;
}
