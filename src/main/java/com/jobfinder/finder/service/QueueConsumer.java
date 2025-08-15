package com.jobfinder.finder.service;

import com.jobfinder.finder.config.rabbitMq.RabbitMQConstants;
import com.jobfinder.finder.dto.post.PostStatusChangeQueueMessage;
import com.jobfinder.finder.dto.submission.SubmissionStatusQueueMessage;
import com.jobfinder.finder.dto.user.UserStatusChangeQueueMessage;
import com.jobfinder.finder.dto.user.VerificationTokenMessage;
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
  private static final String USER_STATUS_CHANGED_SUBJECT = "User Status Changed";
  private static final String USER_REGISTRATION_SUBJECT = "Verify Your Email";

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

  @RabbitListener(queues = RabbitMQConstants.USER_STATUS_CHANGED_QUEUE)
  public void consumeMessageForUserStatusChange(Object message) {
    if (message instanceof UserStatusChangeQueueMessage) {
      UserStatusChangeQueueMessage userStatusMessage = (UserStatusChangeQueueMessage) message;
      // Process the message
      System.out.println("Consumed message: " + message);

      String email = userStatusMessage.getEmail();
      Optional<UserEntity> UserEntityOptional = userRepository.findByUsernameOrEmail(null, email);
      if (UserEntityOptional.isEmpty()) {
        throw new UsernameNotFoundException("User with email " + email + " not found");
      }
      UserEntity user = UserEntityOptional.get();
      emailService.sendEmail(email,
          USER_STATUS_CHANGED_SUBJECT,
          String.format("User: %s status has changed to: %s ", userStatusMessage.getEmail(), userStatusMessage.getUserStatus()));
    }
  }

  @RabbitListener(queues = RabbitMQConstants.USER_REGISTRATION_CHANGED_QUEUE)
  public void consumeMessageForUserRegistration(Object message) {
    if (message instanceof VerificationTokenMessage) {
      VerificationTokenMessage verificationTokenMessage = (VerificationTokenMessage) message;
      // Process the message
      System.out.println("Consumed message: " + message);

      String email = verificationTokenMessage.getEmail();
      Optional<UserEntity> UserEntityOptional = userRepository.findByUsernameOrEmail(null, email);
      if (UserEntityOptional.isEmpty()) {
        throw new UsernameNotFoundException("User with email: " + email + " not found");
      }
      emailService.sendEmail(email,
          USER_REGISTRATION_SUBJECT,
          String.format("Verify your account by clicking on this url: %s ",
              "http://localhost:8080/user/verify?token="
                  + verificationTokenMessage.getToken())); //todo: create an endpoint for verification and depend on environment variable for the URL to be changed after deploying
    }
  }
}


