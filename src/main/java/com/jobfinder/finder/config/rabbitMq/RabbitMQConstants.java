package com.jobfinder.finder.config.rabbitMq;

public class RabbitMQConstants {
  public static final String SUBMISSION_STATUS_EXCHANGE = "submission.status.exchange";
  public static final String SUBMISSION_STATUS_CHANGED_ROUTING_KEY = "submission.status.changed";
  public static final String SUBMISSION_STATUS_CHANGED_QUEUE = SUBMISSION_STATUS_CHANGED_ROUTING_KEY + ".queue";

  public static final String POST_STATUS_EXCHANGE = "post.status.exchange";
  public static final String POST_STATUS_CHANGED_ROUTING_KEY = "post.status.changed";
  public static final String POST_STATUS_CHANGED_QUEUE = POST_STATUS_CHANGED_ROUTING_KEY + ".queue";

  public static final String USER_STATUS_EXCHANGE = "user.status.exchange";
  public static final String USER_STATUS_CHANGED_ROUTING_KEY = "user.status.changed";
  public static final String USER_STATUS_CHANGED_QUEUE = USER_STATUS_CHANGED_ROUTING_KEY + ".queue";

  public static final String USER_REGISTRATION_EXCHANGE = "user.registration.exchange";
  public static final String USER_REGISTRATION_CHANGED_ROUTING_KEY = "user.registration.changed";
  public static final String USER_REGISTRATION_CHANGED_QUEUE = USER_REGISTRATION_CHANGED_ROUTING_KEY + ".queue";
}
