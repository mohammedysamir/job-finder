package com.jobfinder.finder.service;

import com.jobfinder.finder.config.rabbitMq.RabbitMQConstants;
import com.jobfinder.finder.dto.post.PostStatusChangeQueueMessage;
import com.jobfinder.finder.dto.submission.SubmissionStatusQueueMessage;
import com.jobfinder.finder.entity.UserEntity;
import com.jobfinder.finder.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.jobfinder.finder.config.rabbitMq.RabbitMQConstants.SUBMISSION_STATUS_CHANGED_QUEUE;

@Service
@RequiredArgsConstructor
public class QueueConsumer {
  private final UserRepository userRepository;
  private final EmailService emailService;
  private static final String SUBMISSION_STATUS_CHANGED_SUBJECT = "Submission Status Changed";
  private static final String POST_STATUS_CHANGED_SUBJECT = "Post Status Changed";

  @RabbitListener(queues = SUBMISSION_STATUS_CHANGED_QUEUE)
  public void consumeMessageForSubmissionStatusChange(Object message) {
    if (message instanceof SubmissionStatusQueueMessage) {
      SubmissionStatusQueueMessage submissionStatusMessage = (SubmissionStatusQueueMessage) message;
      // Process the message
      System.out.println("Consumed message: " + message);

      String username = submissionStatusMessage.getUsername();
      Optional<UserEntity> UserEntityOptional = userRepository.findByUsername(username);
      if (UserEntityOptional.isEmpty()) {
        throw new UsernameNotFoundException("User with username " + username + " not found");
      }
      UserEntity user = UserEntityOptional.get();
      emailService.sendEmail(user.getEmail(),
          SUBMISSION_STATUS_CHANGED_SUBJECT,
          String.format("Your submission: %s status has changed to: %s ", submissionStatusMessage.getSubmissionId(), submissionStatusMessage.getStatus()));
    }
  }

  @RabbitListener(queues = RabbitMQConstants.POST_STATUS_CHANGED_QUEUE)
  public void consumeMessageForPostStatusChange(Object message) {
    if (message instanceof PostStatusChangeQueueMessage) {
      PostStatusChangeQueueMessage postStatusMessage = (PostStatusChangeQueueMessage) message;
      // Process the message
      System.out.println("Consumed message: " + message);

      String username = postStatusMessage.getUsername();
      Optional<UserEntity> UserEntityOptional = userRepository.findByUsername(username);
      if (UserEntityOptional.isEmpty()) {
        throw new UsernameNotFoundException("User with username " + username + " not found");
      }
      UserEntity user = UserEntityOptional.get();
      emailService.sendEmail(user.getEmail(),
          POST_STATUS_CHANGED_SUBJECT,
          String.format("Post: %s status has changed to: %s ", postStatusMessage.getPostId(), postStatusMessage.getPostStatus()));
    }
  }
}


