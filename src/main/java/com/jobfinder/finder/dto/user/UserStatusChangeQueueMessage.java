package com.jobfinder.finder.dto.user;

import com.jobfinder.finder.constant.PostStatus;
import com.jobfinder.finder.constant.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserStatusChangeQueueMessage {
  String email;
  UserStatus userStatus;
}
