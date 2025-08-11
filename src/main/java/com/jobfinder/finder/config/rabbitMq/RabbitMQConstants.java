package com.jobfinder.finder.config.rabbitMq;

public class RabbitMQConstants {
  public static final String SUBMISSION_STATUS_EXCHANGE = "submission.status.exchange";
  public static final String SUBMISSION_STATUS_CHANGED_ROUTING_KEY = "submission.status.changed";
  public static final String SUBMISSION_STATUS_CHANGED_QUEUE = SUBMISSION_STATUS_CHANGED_ROUTING_KEY + ".queue";

  public static final String POST_STATUS_EXCHANGE = "post.status.exchange";
  public static final String POST_STATUS_CHANGED_ROUTING_KEY = "post.status.changed";
  public static final String POST_STATUS_CHANGED_QUEUE = POST_STATUS_CHANGED_ROUTING_KEY + ".queue";
}
