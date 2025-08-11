package com.jobfinder.finder.dto.admin;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminResponseDto implements Serializable {
  private String username;
  private String email;
}
