package com.jobfinder.finder.dto.user;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VerificationTokenMessage implements Serializable {
  String token;
  String email;
}
