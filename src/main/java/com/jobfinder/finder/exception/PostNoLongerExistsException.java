package com.jobfinder.finder.exception;

public class PostNoLongerExistsException extends RuntimeException {
  String message;
  static String prefix = "The post you are trying to access no longer exists: ";
  public PostNoLongerExistsException(String message) {
    super(String.format(prefix,message));
    this.message = String.format(prefix,message);
  }

  @Override
  public String getMessage() {
    return message;
  }
}
