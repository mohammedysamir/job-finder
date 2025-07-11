package com.jobfinder.finder.exception;

public class UnauthorizedException extends RuntimeException {
  static String message = "You are not authorized to perform this action";

  public UnauthorizedException() {
    super(message);
  }

  @Override
  public String getMessage() {
    return message;
  }
}
