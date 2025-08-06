package com.jobfinder.finder.exception;

public class AdminNotFoundException extends RuntimeException {
  String message;
  static String prefix = "An admin with this username %s doesn't exist";
  public AdminNotFoundException(String message) {
    super(String.format(prefix,message));
    this.message = String.format(prefix,message);
  }

  @Override
  public String getMessage() {
    return message;
  }
}
