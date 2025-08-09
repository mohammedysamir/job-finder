package com.jobfinder.finder.dto.post;

import com.jobfinder.finder.constant.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostStatusChangeQueueMessage {
  Long postId;
  PostStatus postStatus;
  String username;
}
