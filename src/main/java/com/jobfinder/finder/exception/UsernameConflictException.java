package com.jobfinder.finder.exception;

public class UsernameConflictException extends RuntimeException {
  String message;
  static String prefix = "This username %s exists";
  public UsernameConflictException(String message) {
    super(String.format(prefix,message));
    this.message = String.format(prefix,message);
  }

  @Override
  public String getMessage() {
    return message;
  }
}
