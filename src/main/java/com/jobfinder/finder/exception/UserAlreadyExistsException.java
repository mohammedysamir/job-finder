package com.jobfinder.finder.exception;

public class UserAlreadyExistsException extends RuntimeException {
  String message;
  static String prefix = "A user with the email or username %s already exists";
  public UserAlreadyExistsException(String message) {
    super(String.format(prefix,message));
    this.message = String.format(prefix,message);
  }

  @Override
  public String getMessage() {
    return message;
  }
}
